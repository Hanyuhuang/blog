package com.hyh.pojo.Bo;

import com.hyh.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeBo implements Serializable {

    private User sender;
    private Long receiver;
    private Integer type;

}
