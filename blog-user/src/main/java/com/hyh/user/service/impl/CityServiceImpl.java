package com.hyh.user.service.impl;

import com.hyh.pojo.City;
import com.hyh.pojo.Vo.CityVo;
import com.hyh.user.mapper.CityMapper;
import com.hyh.user.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<City> queryCityByType(Integer type) {
        Example example = new Example(City.class);
        example.createCriteria().orEqualTo("type",type);
        return cityMapper.selectByExample(example);
    }

    @Override
    public List<City> queryCityByPid(Integer pid) {
        Example example = new Example(City.class);
        example.createCriteria().orEqualTo("pid",pid);
        return cityMapper.selectByExample(example);
    }

    @Override
    public List<CityVo> listAllCity() {
        // 查询缓存
        List<CityVo> cityList = redisTemplate.boundHashOps("city").values();
        if (cityList == null || cityList.size() == 0) {
            // 查询数据库
            cityList = cityMapper.listAllCity();
            // list转成map 放入缓存
            Map<String,CityVo> map = cityList.stream().collect(Collectors.toMap(cityVo -> cityVo.getId()+"",cityVo->cityVo));
            redisTemplate.boundHashOps("city").putAll(map);
        }
        return cityMapper.listAllCity();
    }

}
