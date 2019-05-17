package com.hyh.user.controller;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hyh.user.service.UserService;


@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        try {
            User user =  userService.getUserById(id);
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Integer> checkEmail(@PathVariable("email") String email){
        try {
            int result =  userService.countUserByEmail(email);
            if (result>0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Integer> checkPhone(@PathVariable("phone") String phone){
        try {
            int result =  userService.countUserByPhone(phone);
            if (result>0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<User> getUserByLoginName(@RequestParam String loginName,@RequestParam String password){
        try {
            User user =  userService.getUserByLoginName(loginName,password);
            if (user==null) new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/code")
    public ResponseEntity<Integer> getCode(String email){
        try{
            userService.getCode(email);
            return  ResponseEntity.ok(null);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Integer> insertUser(@RequestBody UserVo userVo){
        try{
            int result = userService.saveUser(userVo.getUser(),userVo.getCode());
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(null);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<Integer> updateUser(@RequestBody User user){
        try {
            int result = userService.updateUser(user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable("id") Long id){
        try {
            int result = userService.deleteUserById(id);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
