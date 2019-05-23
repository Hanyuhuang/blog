package com.hyh.pojo.Vo;

import com.hyh.pojo.User;
import lombok.Data;


@Data
public class UserVo extends User{
    private String code;
    private String newPassword;
    private String newEmail;
    private String newPhone;
    private User user;
}
