package com.hyh.pojo.Bo;

import com.hyh.pojo.Comment;
import com.hyh.pojo.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentBo implements Serializable {

    private Comment comment;
    private User user;
}
