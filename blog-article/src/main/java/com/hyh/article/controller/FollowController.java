package com.hyh.article.controller;

import com.hyh.article.service.FollowService;
import com.hyh.article.service.StarService;
import com.hyh.pojo.Bo.FollowBo;
import com.hyh.pojo.Follow;
import com.hyh.pojo.Star;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin(value = {"http://localhost:8080","http://localhost:8081","http://localhost:8082"},allowCredentials = "true")
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    FollowService followService;


    /**
     * 查询我的收藏列表
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<PageResult<ArticleVo>> listFollows(Integer pageCur, Integer pageSize, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageResult<ArticleVo> result = followService.listFollows(pageCur,pageSize,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 收藏
     * @param followBo
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<Integer> insertFollow(@RequestBody FollowBo followBo, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = followService.insertFollow(followBo,user);
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据文章id取消点赞
     * @param articleId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteFollowByArticleId(@PathVariable("id") Long articleId,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = followService.deleteFollowByArticleId(articleId,user);
            if (result <1 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据id批量删除收藏记录
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Integer> deleteFollowsByArticleIds(Long[] ids,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            int result = followService.deleteFollowsByArticleIds(ids,user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
