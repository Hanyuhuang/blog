package com.hyh.article.service;

import com.hyh.pojo.Bo.CommentBo;
import com.hyh.pojo.User;

public interface CommentService {

    int insertComment(CommentBo commentBo, User user);
}
