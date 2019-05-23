package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "view")
public class View implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;        //浏览id
    private Long userId;    //浏览用户
    private Long articleId; //浏览文章
    private Date time;      //浏览时间
}
