package com.hyh.article.service.impl;

import com.hyh.article.mapper.CommentMapper;
import com.hyh.article.service.CommentService;
import com.hyh.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public int insertComment(Comment comment) {
        comment.setCreateTime(new Date());
        return commentMapper.insert(comment);
    }
}
