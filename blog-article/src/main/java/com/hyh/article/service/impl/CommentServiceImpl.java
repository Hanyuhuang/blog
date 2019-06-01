package com.hyh.article.service.impl;

import com.hyh.article.mapper.CommentMapper;
import com.hyh.article.mapper.NoticeMapper;
import com.hyh.article.service.CommentService;
import com.hyh.pojo.Bo.CommentBo;
import com.hyh.pojo.Comment;
import com.hyh.pojo.Notice;
import com.hyh.pojo.User;

import com.hyh.pojo.Bo.NoticeBo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public int insertComment(CommentBo commentBo, User user) {
        // 删除缓存
        redisTemplate.boundHashOps("article").delete(commentBo.getComment().getArticleId()+"");
        // 添加评论
        commentBo.getComment().setUserId(user.getId());
        commentBo.getComment().setCreateTime(new Date());
        int result = commentMapper.insert(commentBo.getComment());
        // 添加通知
        Notice notice = new Notice(user.getId(),commentBo.getUser().getId(),commentBo.getComment().getArticleId(),0,3,new Date());
        int count = noticeMapper.insert(notice);
        if (count>0){
            // 封装通知对象 并 发送通知
            NoticeBo noticeBo = new NoticeBo(user,commentBo.getUser().getId(),3);
            amqpTemplate.convertAndSend("blog-notice-exchange","notice", noticeBo);
        }
        return result;
    }

    @Override
    public int deleteCommentById(Long id) {
        // 删除缓存
        Comment comment = commentMapper.selectByPrimaryKey(id);
        redisTemplate.boundHashOps("article").delete(comment.getArticleId()+"");
        return commentMapper.deleteByPrimaryKey(id);
    }
}
