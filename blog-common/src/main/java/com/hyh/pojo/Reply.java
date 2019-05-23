package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="reply")
public class Reply implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;          //回复id
    private Long userId;      //回复人
    private Long commentId;   //回复评论编号
    private String content;   //回复内容
    private Date createTime;  //回复时间
    private Date updateTime;  //修改回复的时间
}
