package com.hyh.user.service;

import com.hyh.pojo.User;

public interface UserService {

    int saveUser(User user,String code);

    int deleteUserById(Long id);

    int updateUser(User user);

    User getUserById(Long id);

    User getUserByLoginName(String loginName, String password);

    void getCode(String email);

    int countUserByEmail(String email);

    int countUserByPhone(String phone);

    int updatePassword(Long id,String oldPassword, String newPassword);

    String getUserNameById(Long id);
}
