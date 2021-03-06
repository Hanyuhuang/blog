package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.client.UserClient;
import com.hyh.article.mapper.ArticleMapper;
import com.hyh.article.mapper.FollowMapper;
import com.hyh.article.mapper.NoticeMapper;
import com.hyh.article.service.FollowService;
import com.hyh.pojo.Article;
import com.hyh.pojo.Bo.FollowBo;
import com.hyh.pojo.Bo.NoticeBo;
import com.hyh.pojo.Follow;
import com.hyh.pojo.Notice;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public int insertFollow(FollowBo followBo,User user) {
        // 删除缓存
        redisTemplate.boundHashOps("article").delete(followBo.getFollow().getArticleId()+"");
        // 添加点赞记录
        followBo.getFollow().setUserId(user.getId());
        followBo.getFollow().setTime(new Date());
        int result =  followMapper.insert(followBo.getFollow());
        // 添加通知记录
        Notice notice = new Notice(user.getId(), followBo.getUser().getId(),followBo.getFollow().getArticleId(),0,2,new Date());
        int count = noticeMapper.insert(notice);
        if (count>0){
            // 封装通知 并发送
            NoticeBo noticeBo = new NoticeBo(user, followBo.getUser().getId(),2);
            amqpTemplate.convertAndSend("blog-notice-exchange","notice", noticeBo);
        }
        return result;
    }

    @Override
    public int deleteFollowByArticleId(Long articleId,User user) {
        // 删除缓存
        redisTemplate.boundHashOps("article").delete(articleId+"");
        Example example = new Example(Follow.class);
        example.createCriteria().andEqualTo("articleId",articleId).andEqualTo("userId",user.getId());
        return followMapper.deleteByExample(example);
    }

    @Override
    public PageResult<ArticleVo> listFollows(Integer pageCur, Integer pageSize, User user) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 查询我的点赞列表
        Example example = new Example(Follow.class);
        example.createCriteria().andEqualTo("userId",user.getId());
        example.setOrderByClause("time DESC");
        // 查询
        Page<Follow> followList = (Page<Follow>) followMapper.selectByExample(example);
        // 根据查询结果 封装文章Vo
        List<ArticleVo> result = followList.stream().map(follow -> {
            ArticleVo articleVo = new ArticleVo();
            // 查询文章
            Article article = articleMapper.selectByPrimaryKey(follow.getArticleId());
            articleVo.setArticle(article);
            // 查询文章作者
            ResponseEntity<User> author = userClient.getUserById(article.getUserId());
            articleVo.setUser(author.getBody());
            return articleVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<ArticleVo>(result.size(),result);
    }

    @Override
    public int deleteFollowsByArticleIds(Long[] ids, User user) {
        int count = 0;
        for (Long id: ids) {
            count += deleteFollowByArticleId(id,user);
        }
        return count;
    }
}
