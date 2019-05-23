package com.hyh.article.client;

import com.hyh.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user-service")
@RequestMapping("user")
public interface UserClient {

    @GetMapping("/{id}")
    User getUserById(@PathVariable("id") Long id);
}
