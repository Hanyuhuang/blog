package com.hyh.user.controller;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hyh.user.service.UserService;

import javax.servlet.http.HttpSession;


@CrossOrigin(value = {"http://localhost:8080","http://localhost:8081","http://localhost:8082"},allowCredentials = "true")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
        /*try {
            User user =  userService.getUserById(id);
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
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
            if (result>0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            if (result>0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            User user =  userService.getUserByLoginName(loginName,password);
            // 用户不存在
            if (user==null) new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
           session.invalidate();
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
    @PostMapping
    public ResponseEntity<Integer> insertUser(@RequestBody UserVo userVo){
        try{
            int result = userService.saveUser(userVo.getUser(),userVo.getCode());
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(null);
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
    public ResponseEntity<Integer> updateUser(@RequestBody User user){
        try {
            int result = userService.updateUser(user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            System.out.println(user);
            int result = userService.updatePassword(user.getId(),user.getPassword(),user.getNewPassword());
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteUserById(@PathVariable("id") Long id){
        try {
            int result = userService.deleteUserById(id);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
