package com.hyh.pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private long total;
    private int totalPage;
    private List<T> items;

    public PageResult(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
    public PageResult(List<T> items) {
        this.items = items;
    }
}
