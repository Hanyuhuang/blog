package com.hyh.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;          // id
    private String name;      // 用户名
    private String password;  // 密码
    private Date birthday;    // 生日
    private String sex;       // 性别
    private String phone;     // 手机号
    private String email;     // 邮箱
    private String address;   // 地址

}
