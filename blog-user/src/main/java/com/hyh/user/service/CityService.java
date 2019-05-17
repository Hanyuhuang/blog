package com.hyh.user.service;


import com.hyh.pojo.City;
import com.hyh.pojo.Vo.CityVo;

import java.util.List;

public interface CityService {

    List<City> queryCityByType(Integer type);

    List<City> queryCityByPid(Integer pid);

    List<CityVo> listAllCity();

}
