package com.hyh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "view")
public class View implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;        //浏览id
    private Long userId;    //浏览用户
    private Long articleId; //浏览文章
    private Integer status; //浏览状态
    private Date createTime;//创建时间
    private Date updateTime;//浏览时间


    public View(Long userId, Long articleId,Integer status, Date time) {
        this.userId = userId;
        this.articleId = articleId;
        this.status = status;
        this.createTime = time;
    }
}
