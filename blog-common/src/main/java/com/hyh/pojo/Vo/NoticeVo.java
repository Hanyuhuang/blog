package com.hyh.pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVo implements Serializable {

    private Long id;
    private Long articleId;
    private String articleName;
    private String senderName;
    private String receiverName;
    private Integer type;
}
