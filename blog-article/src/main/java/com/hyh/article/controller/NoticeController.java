package com.hyh.article.controller;

import com.hyh.article.service.NoticeService;
import com.hyh.pojo.Bo.NoticesBo;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.NoticeVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity<PageResult<NoticeVo>> listMyNotices(Integer pageCur, Integer pageSize, Integer status,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageResult<NoticeVo> result = noticeService.listMyNotices(pageCur,pageSize,status,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countNotice(HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            System.out.println(user);
            int result = noticeService.countNotice(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Integer> updateNotices(@RequestBody NoticesBo noticesBo){
        try {
            int result = noticeService.updateNotices(noticesBo.getIds(),noticesBo.getStatus());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteNotices(Long[] ids){
        try {
            int result = noticeService.deleteNotices(ids);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
