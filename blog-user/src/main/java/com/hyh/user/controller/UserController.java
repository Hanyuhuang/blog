package com.hyh.user.controller;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hyh.user.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
        try {
            User user =  userService.getUserById(id);
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/name/{id}")
    public ResponseEntity<String> getUserNameById(@PathVariable("id") Long id){
        try {
            String user =  userService.getUserNameById(id);
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询email是否存在
     * @param email
     * @return
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Integer> checkEmail(@PathVariable("email") String email){
        try {
            int result =  userService.countUserByEmail(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改邮箱
     * @param userVo
     * @return
     */
    @PutMapping("/email")
    public ResponseEntity<Integer> updateEmail(@RequestBody UserVo userVo,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            if (user==null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            userVo.setId(user.getId());
            int result =  userService.updateEmail(userVo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 修改手机
     * @param userVo
     * @return
     */
    @PutMapping("/phone")
    public ResponseEntity<Integer> updatePhone(@RequestBody UserVo userVo,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            if (user==null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            userVo.setId(user.getId());
            int result =  userService.updatePhone(userVo);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询手机是否存在
     * @param phone
     * @return
     */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Integer> checkPhone(@PathVariable("phone") String phone){
        try {
            int result =  userService.countUserByPhone(phone);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户登录
     * @param loginName
     * @param password
     * @param session
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<User> getUserByLoginName(@RequestParam String loginName,@RequestParam String password,
                HttpSession session){
        try {
            System.out.println(loginName+""+password);
            User user =  userService.getUserByLoginName(loginName,password);
            // 用户不存在
            if (user==null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // 放入session
            session.setAttribute("user",user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户注销退出
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity logout(HttpSession session){
        try {
            session.removeAttribute("user");
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 发送验证码到邮箱
     * @param email
     * @return
     */
    @GetMapping("/code")
    public ResponseEntity<Integer> getCode(String email){
        try{
            userService.getCode(email);
            return  ResponseEntity.ok(null);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 用户注册
     * @param userVo
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Integer> insertUser(@RequestBody UserVo userVo){
        try{
            int result = userService.saveUser(userVo);
            return ResponseEntity.ok(result);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping
    public ResponseEntity<Integer> updateUser(@RequestBody User user,HttpSession session){
        try {
            User curUser = (User) session.getAttribute("user");
            if (curUser==null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            user.setId(curUser.getId());
            int result = userService.updateUser(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改用户密码
     * @param user
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<Integer> updatePassword(@RequestBody UserVo user){
        try {
            int result = userService.updatePassword(user.getId(),user.getPassword(),user.getNewPassword());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
