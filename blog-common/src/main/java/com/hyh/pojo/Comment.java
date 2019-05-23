package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;        //评论编号
    private Long articleId; //评论文章
    private Long userId;    //评论人
    private String content; //评论内容
    private Date createTime;//评论时间
    private Date updateTime;//更新时间
}
