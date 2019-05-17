package com.hyh.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "city")
public class City implements Serializable {
    @Id
    private Integer id;
    private Integer pid;
    private String name;
    private Integer type;
}
