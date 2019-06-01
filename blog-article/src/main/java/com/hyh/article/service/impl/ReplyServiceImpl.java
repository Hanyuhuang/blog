package com.hyh.article.service.impl;

import com.hyh.article.mapper.CommentMapper;
import com.hyh.article.mapper.ReplyMapper;
import com.hyh.article.service.ReplyService;
import com.hyh.pojo.Comment;
import com.hyh.pojo.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public int insertReply(Reply reply){
        // 查询回复文章
        Comment comment = commentMapper.selectByPrimaryKey(reply.getCommentId());
        // 删除缓存
        redisTemplate.boundHashOps("article").delete(comment.getArticleId()+"");
        reply.setCreateTime(new Date());
        return replyMapper.insert(reply);
    }

    @Override
    public int deleteReplyById(Long id) {
        // 删除缓存
        Reply reply = replyMapper.selectByPrimaryKey(id);
        Comment comment = commentMapper.selectByPrimaryKey(reply.getCommentId());
        redisTemplate.boundHashOps("article").delete(comment.getArticleId()+"");
        return replyMapper.deleteByPrimaryKey(id);
    }
}
