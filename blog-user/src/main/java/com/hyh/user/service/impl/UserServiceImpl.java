package com.hyh.user.service.impl;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.UserVo;
import com.hyh.user.mapper.UserMapper;
import com.hyh.user.service.UserService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 用户注册
     * @param user  用户信息
     * @return
     */
    @Override
    public int saveUser(UserVo user) {
        // 获取验证码 验证
        String realCode = (String) redisTemplate.boundValueOps(user.getEmail()).get();
        if (realCode==null || realCode.length()!=6 || !user.getCode().equals(realCode)) return 0;
        // MD5加密
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
        // 给用户设置一些默认信息
        user.setSex("男");
        user.setBirthday(new Date());
        user.setImage("https://i.loli.net/2017/08/21/599a521472424.jpg");
        int result = userMapper.insert(user);
        return result;
    }

    /**
     * 根据用户id删除用户
     * @param id
     * @return
     */
    @Override
    public int deleteUserById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public int updateUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user!=null) user.setPassword(null);
        return user;
    }

    /**
     * 用户登录
     * @param loginName 登录名 可以为id、手机、邮箱
     * @param password  密码
     * @return
     */
    @Override
    public User getUserByLoginName(String loginName, String password) {
        // MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userMapper.getUserByLoginName(loginName,password);
        System.out.println(user);
        if (user!=null) user.setPassword(null);
        return user;
    }

    /**
     * 获取验证码
     * @param email 用户邮箱
     */
    @Override
    public void sendCode(String email) {
        // 放入消息队列 发送邮件
        amqpTemplate.convertAndSend("blog-email-code-exchange","email.code",email);
    }

    /**
     * 查询邮箱存不存在
     * @param email
     * @return  存在返回1 不存在返回0
     */
    @Override
    public int countUserByEmail(String email) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email",email);
        return userMapper.selectCountByExample(example);
    }

    /**
     * 查询手机存不存在
     * @param phone
     * @return  存在返回1 不存在返回0
     */
    @Override
    public int countUserByPhone(String phone) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("phone",phone);
        return userMapper.selectCountByExample(example);
    }

    /**
     * 修改用户密码
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public int updatePassword(Long id,String oldPassword, String newPassword) {
        // 查询是否存在
        User user = this.getUserByLoginName(id+"",oldPassword);
        // 用户不存在 或 密码错误
        if (user == null) return 0;
        // 密码加密 写入数据库
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        user.setPassword(newPassword);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public String getUserNameById(Long id){
        return userMapper.getUserNameById(id);
    }

    @Override
    public int updatePhone(UserVo userVo) {
        User user = new User();
        user.setId(userVo.getId());
        user.setPhone(userVo.getNewPhone());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateEmail(UserVo userVo) {
        // 获取验证码 验证
        String realCode = (String) redisTemplate.boundValueOps(userVo.getNewEmail()).get();
        if (realCode==null || realCode.length()!=6 || !userVo.getCode().equals(realCode)) return 0;
        User user = new User();
        user.setId(userVo.getId());
        user.setEmail(userVo.getNewEmail());
        return userMapper.updateByPrimaryKeySelective(user);
    }

}
