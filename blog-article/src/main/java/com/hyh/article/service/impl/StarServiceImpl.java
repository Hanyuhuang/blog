package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.mapper.ArticleMapper;
import com.hyh.article.mapper.NoticeMapper;
import com.hyh.article.mapper.StarMapper;
import com.hyh.article.service.StarService;
import com.hyh.pojo.Article;
import com.hyh.pojo.Notice;
import com.hyh.pojo.Star;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Bo.NoticeBo;
import com.hyh.pojo.Vo.PageResult;
import com.hyh.pojo.Bo.StarBo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StarServiceImpl implements StarService {

    @Autowired
    private StarMapper starMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public int insertStar(StarBo starBo, User user) {
        // 添加点赞记录
        starBo.getStar().setUserId(user.getId());
        starBo.getStar().setTime(new Date());
        int result =  starMapper.insert(starBo.getStar());
        // 添加通知记录
        Notice notice = new Notice(user.getId(), starBo.getUser().getId(),starBo.getStar().getArticleId(),0,1,new Date());
        int count = noticeMapper.insert(notice);
        if (count>0){
            // 封装通知 并发送
            NoticeBo noticeBo = new NoticeBo(user, starBo.getUser().getId(),1);
            amqpTemplate.convertAndSend("blog-notice-exchange","notice", noticeBo);
        }
        return result;
    }


    @Override
    public int deleteStarByArticleId(Long articleId,User user) {
        Example example = new Example(Star.class);
        example.createCriteria().andEqualTo("articleId",articleId).andEqualTo("userId",user.getId());
        return starMapper.deleteByExample(example);
    }

    @Override
    public PageResult<ArticleVo> listStars(Integer pageCur, Integer pageSize, User user) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 查询我的点赞列表
        Example example = new Example(Star.class);
        example.createCriteria().andEqualTo("userId",user.getId());
        // 查询
        Page<Star> starList = (Page<Star>) starMapper.selectByExample(example);
        // 根据查询结果 封装文章Vo
        List<ArticleVo> result = starList.stream().map(star -> {
            ArticleVo articleVo = new ArticleVo();
            // 查询文章
            Article article = articleMapper.selectByPrimaryKey(star.getArticleId());
            articleVo.setArticle(article);
            articleVo.setUser(user);
            return articleVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<ArticleVo>(result.size(),result);
    }

    @Override
    public int deleteStarsByArticleIds(Long[] ids, User user) {
        int count = 0;
        for (Long id: ids) {
            count += deleteStarByArticleId(id,user);
        }
        return count;
    }
}
