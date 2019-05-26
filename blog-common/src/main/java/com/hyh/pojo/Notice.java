package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "notice")
public class Notice implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;        //通知id
    private Long senderId;  //发送人
    private Long receiverId;//接收人
    private Integer status; //通知状态 0未读 1已读
    private Integer type;   //通知类型 1点赞 2收藏 3评论
    private Date createTime;//创建时间

    public Notice(Long senderId, Long receiverId, Integer status, Integer type, Date createTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
        this.type = type;
        this.createTime = createTime;
    }
}
