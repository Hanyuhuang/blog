package com.hyh.user.service;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.UserVo;

public interface UserService {

    int saveUser(UserVo userVo);

    int deleteUserById(Long id);

    int updateUser(User user);

    User getUserById(Long id);

    User getUserByLoginName(String loginName, String password);

    void sendCode(String email);

    int countUserByEmail(String email);

    int countUserByPhone(String phone);

    int updatePassword(Long id,String oldPassword, String newPassword);

    String getUserNameById(Long id);

    int updatePhone(UserVo userVo);

    int updateEmail(UserVo userVo);
}
