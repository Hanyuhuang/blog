package com.hyh.user.mapper;

import com.hyh.pojo.City;
import com.hyh.pojo.Vo.CityVo;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CityMapper extends Mapper<City> {

    @Results(id = "cityMap",value = {@Result(column = "id",property = "id",javaType = Integer.class),
            @Result(column = "pid",property = "pid",javaType = Integer.class),
            @Result(column = "name",property = "label",javaType = String.class),
            @Result(column = "name",property = "value",javaType = String.class),
            @Result(column = "id",property = "children",many = @Many(select = "com.hyh.user.mapper.CityMapper.queryCityByPid"))
            })
    @Select("select * from city where type = 1")
    List<CityVo> listAllCity();

    @ResultMap("cityMap")
    @Select("select * from city where pid = #{pid} and type<=3")
    List<CityVo> queryCityByPid(Integer pid);
}
