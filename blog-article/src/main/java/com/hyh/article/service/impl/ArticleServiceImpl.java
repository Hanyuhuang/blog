package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.client.UserClient;
import com.hyh.article.mapper.*;
import com.hyh.article.service.ArticleService;
import com.hyh.pojo.*;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.CommentVo;
import com.hyh.pojo.Vo.PageResult;
import com.hyh.pojo.Vo.ReplyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StarMapper starMapper;
    @Autowired
    private ViewMapper viewMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private UserClient userClient;

    @Override
    public int saveArticle(Article article) {
        article.setCreateTime(new Date());
        return articleMapper.insert(article);
    }

    @Override
    public PageResult<Article> listArticlesByPage(Integer pageCur, Integer pageSize, String orderBy, Boolean desc, String keywords) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 过滤
        Example example = new Example(Article.class);
        if (StringUtils.isNotBlank(keywords)) {
            example.createCriteria().andLike("title", "%" + keywords + "%")
                    .orLike("tag","%" + keywords + "%")
                    .orLike("content", "%" + keywords + "%");
        }
        if (StringUtils.isNotBlank(orderBy)) {
            // 排序
            String orderByClause = orderBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        //example.createCriteria().andEqualTo("userId",)
        // 查询
        Page<Article> result = (Page<Article>) articleMapper.selectByExample(example);
        // 返回结果
        return new PageResult<Article>(result.getTotal(),result.getPageSize(), result);
    }

    @Override
    public PageResult<ArticleVo> listRecentArticles() {
        // 开始分页
        PageHelper.startPage(1, 5);
        Example example = new Example(Article.class);
        // 排序
        example.setOrderByClause("create_time DESC");
        // 查询
        Page<Article> articleResult = (Page<Article>) articleMapper.selectByExample(example);
        // 封装数据
        List<ArticleVo> list = articleResult.getResult().stream().map(item -> {
            // 创建Vo
            ArticleVo articleVo = new ArticleVo();
            // 设置文章
            articleVo.setArticle(item);
            // 设置作者
            User user = userClient.getUserById(item.getUserId());
            articleVo.setUser(user);
            // 设置浏览数
            Example viewExample = new Example(View.class);
            viewExample.createCriteria().andEqualTo("articleId",item.getId());
            articleVo.setStars(viewMapper.selectCountByExample(viewExample));
            // 设置点赞数
            Example starExample = new Example(Star.class);
            starExample.createCriteria().andEqualTo("articleId",item.getId());
            articleVo.setStars(starMapper.selectCountByExample(starExample));
            // 设置收藏数
            Example followExample = new Example(Follow.class);
            followExample.createCriteria().andEqualTo("articleId",item.getId());
            articleVo.setStars(followMapper.selectCountByExample(followExample));
            // 设置评论数
            Example commentExample = new Example(Comment.class);
            followExample.createCriteria().andEqualTo("articleId",item.getId());
            articleVo.setComments(commentMapper.selectCountByExample(commentExample));
            return articleVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<ArticleVo>(list);
    }

    @Override
    public PageResult<Article> listHotArticles() {
        // 开始分页
        PageHelper.startPage(1, 5);
        // 过滤
        Example example = new Example(Article.class);
        // 排序
        example.setOrderByClause("update_time DESC");
        // 查询
        Page<Article> result = (Page<Article>) articleMapper.selectByExample(example);
        // 返回结果
        return new PageResult<Article>(result.getTotal(),result.getPageSize(), result);
    }

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @Override
    public ArticleVo getArticleDetail(Long id) {
        ArticleVo articleVo = new ArticleVo();
        // 查询文章内容
        Article article = articleMapper.selectByPrimaryKey(id);
        // 如果没有查询到
        if (article==null) return articleVo;
        articleVo.setArticle(article);
        // 设置作者
        User user = userClient.getUserById(article.getUserId());
        articleVo.setUser(user);
        // 设置浏览数
        Example viewExample = new Example(View.class);
        viewExample.createCriteria().andEqualTo("articleId",id);
        articleVo.setStars(viewMapper.selectCountByExample(viewExample));
        // 设置点赞数
        Example starExample = new Example(Star.class);
        starExample.createCriteria().andEqualTo("articleId",id);
        articleVo.setStars(starMapper.selectCountByExample(starExample));
        // 设置收藏数
        Example followExample = new Example(Follow.class);
        followExample.createCriteria().andEqualTo("articleId",id);
        articleVo.setStars(followMapper.selectCountByExample(followExample));
        // 设置评论
        Example commentExample = new Example(Comment.class);
        commentExample.createCriteria().andEqualTo("articleId",id);
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        // 评论封装
        List<CommentVo> commentVoList = commentList.stream().map(comment -> {
            // 创建评论Vo
            CommentVo commentVo = new CommentVo();
            commentVo.setComment(comment);
            // 设置评论人
            User commentUser = userClient.getUserById(comment.getUserId());
            commentVo.setUser(commentUser);
            // 查询评论的回复内容
            Example replyExample = new Example(Reply.class);
            replyExample.createCriteria().andEqualTo("commentId",comment.getId());
            List<Reply> replyList = replyMapper.selectByExample(replyExample);
            // 评论回复封装
            List<ReplyVo> replyVoList = replyList.stream().map(reply -> {
                // 创建评论回复Vo
                ReplyVo replyVo = new ReplyVo();
                replyVo.setReply(reply);
                // 设置评论人
                User replyUser = userClient.getUserById(reply.getUserId());
                replyVo.setUser(replyUser);
                return replyVo;
            }).collect(Collectors.toList());
            // 设置评论回复
            commentVo.setReplyList(replyVoList);
            return commentVo;
        }).collect(Collectors.toList());
        // 设置评论 和 评论数
        articleVo.setCommentList(commentVoList);
        articleVo.setComments(commentList.size());
        return articleVo;
    }
}
