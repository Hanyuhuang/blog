package com.hyh.pojo.Vo;

import com.hyh.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo extends User{
    private String code;
    private String newPassword;
    private String newEmail;
    private String newPhone;
    private User user;
}
