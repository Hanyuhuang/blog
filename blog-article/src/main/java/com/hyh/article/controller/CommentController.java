package com.hyh.article.controller;

import com.hyh.article.service.CommentService;

import com.hyh.pojo.Bo.CommentBo;
import com.hyh.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping
    public ResponseEntity<Integer> insertComment(@RequestBody CommentBo commentBo, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = commentService.insertComment(commentBo,user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
