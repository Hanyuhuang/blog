package com.hyh.user.service.impl;

import com.hyh.pojo.User;
import com.hyh.user.mapper.UserMapper;
import com.hyh.user.service.UserService;
import org.springframework.amqp.core.AmqpTemplate;
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
     * @param code  验证码
     * @return
     */
    @Override
    public int saveUser(User user,String code) {
        // 获取验证码 验证
        String realCode = (String) redisTemplate.boundValueOps(user.getEmail()).get();
        if (realCode==null || realCode.length()!=6 || !code.equals(realCode)) return -1;
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
        // 删除缓存
        redisTemplate.boundHashOps("user").delete(id+"");
        return userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @Override
    public int updateUser(User user) {
        // 删除缓存
        redisTemplate.boundHashOps("user").delete(user.getId()+"");
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @Override
    public User getUserById(Long id) {
        // 查询缓存
        User user = (User) redisTemplate.boundHashOps("user").get(id+"");
        // 缓存为空
        if (user==null){
            user = userMapper.selectByPrimaryKey(id);
            // 数据库查询到
            if (user!=null){
                user.setPassword(null);
                redisTemplate.boundHashOps("user").put(user.getId()+"",user);
            } else {
                redisTemplate.boundHashOps("user").put(user.getId(),new User());
                redisTemplate.expire("user",1, TimeUnit.MINUTES);
            }
        }
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
        if (user!=null) user.setPassword(null);
        return user;
    }

    /**
     * 获取验证码
     * @param email 用户邮箱
     */
    @Override
    public void getCode(String email) {
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
        boolean result = redisTemplate.boundHashOps("email").hasKey(email);
        // 缓存中没有
        if (!result){
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("email",email);
            int count = userMapper.selectCountByExample(example);
            // 数据库中存在
            if (count>0){
                result = true;
                redisTemplate.boundHashOps("email").put(email,email);
            }
        }
        return result?1:0;
    }

    /**
     * 查询手机存不存在
     * @param phone
     * @return  存在返回1 不存在返回0
     */
    @Override
    public int countUserByPhone(String phone) {
        boolean result = redisTemplate.boundHashOps("phone").hasKey(phone);
        // 缓存中没有
        if (!result){
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("phone",phone);
            int count = userMapper.selectCountByExample(example);
            // 数据库中存在
            if (count>0){
                result = true;
                redisTemplate.boundHashOps("phone").put(phone,phone);
            }
        }
        return result?1:0;
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

}
