package com.hyh.user.controller;

import com.hyh.pojo.City;
import com.hyh.pojo.Vo.CityVo;
import com.hyh.user.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    CityService cityService;

    @GetMapping("/type/{type}")
    public List<City> queryCityByType(@PathVariable("type") Integer type){
        return cityService.queryCityByType(type);
    }

    @GetMapping("/pid/{pid}")
    public List<City> queryCityByPid(@PathVariable("pid") Integer pid){
        return cityService.queryCityByPid(pid);
    }

    @GetMapping("/list")
    public List<CityVo> listAllCity(){ return cityService.listAllCity();}

}
