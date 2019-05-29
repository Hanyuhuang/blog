package com.hyh.user.mapper;

import com.hyh.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Select("select * from user where (id = #{username} or phone = #{username} or email = #{username})" +
            "and password = #{password}")
    User getUserByLoginName(@Param("username") String loginName, @Param("password") String password);

    @Select("select name from user where id = #{id}")
    String getUserNameById(Long id);
}
