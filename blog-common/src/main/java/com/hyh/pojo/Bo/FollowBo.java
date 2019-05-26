package com.hyh.pojo.Bo;

import com.hyh.pojo.Follow;
import com.hyh.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowBo implements Serializable {

    private Follow follow;
    private User user;

}
