package com.hyh.pojo.Bo;

import com.hyh.pojo.Star;
import com.hyh.pojo.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class StarBo implements Serializable {

    private Star star;
    private User user;
}
