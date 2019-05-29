package com.hyh.article.client;

import com.hyh.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("name/{id}")
    ResponseEntity<String> getUserNameById(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    ResponseEntity<User> getUserById(@PathVariable("id") Long id);
}
