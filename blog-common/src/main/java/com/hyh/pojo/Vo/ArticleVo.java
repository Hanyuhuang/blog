package com.hyh.pojo.Vo;

import com.hyh.pojo.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleVo implements Serializable {

    private Article article;
    private int views;
    private int stars;
    private int follows;
    private int comments;
    private User user;
    private List<CommentVo> commentList;

}
