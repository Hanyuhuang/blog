package com.hyh.pojo.Vo;

import com.hyh.pojo.Comment;
import com.hyh.pojo.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommentVo implements Serializable {

    private Comment comment;
    private User user;
    private List<ReplyVo> replyList;
}
