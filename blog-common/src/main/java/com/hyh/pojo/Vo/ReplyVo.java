package com.hyh.pojo.Vo;

import com.hyh.pojo.Reply;
import com.hyh.pojo.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyVo implements Serializable {

    private Reply reply;
    private User user;
}
