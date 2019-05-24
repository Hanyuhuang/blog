package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "article")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;        //文章id
    private Long userId;    //发表人
    private String title;   //文章标题
    private String tag;     //文章标签
    private String image;   //文章配图
    private String content; //文章内容
    private String mdContent; //MD格式的文章内容
    private Date createTime;//写文章时间
    private Date updateTime;//文章修改时间
}
