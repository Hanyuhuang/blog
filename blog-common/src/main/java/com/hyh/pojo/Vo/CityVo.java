package com.hyh.pojo.Vo;

import com.hyh.pojo.City;
import lombok.Data;

import java.util.List;

@Data
public class CityVo extends City {
    private String label;
    private String value;
    private List<CityVo> children;
}
