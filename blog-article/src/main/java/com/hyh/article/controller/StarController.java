package com.hyh.article.controller;

import com.hyh.article.service.StarService;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import com.hyh.pojo.Bo.StarBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/star")
public class StarController {

    @Autowired
    StarService starService;

    /**
     * 查询我的点赞列表
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<PageResult<ArticleVo>> listStars(Integer pageCur, Integer pageSize,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageResult<ArticleVo> result = starService.listStars(pageCur,pageSize,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 点赞
     * @param starBo
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<Integer> insertStar(@RequestBody StarBo starBo, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = starService.insertStar(starBo,user);
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 根据文章id取消点赞
     * @param articleId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteStarByArticleId(@PathVariable("id") Long articleId,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = starService.deleteStarByArticleId(articleId,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据文章id批量取消点赞
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Integer> deleteStarsByArticleIds(Long[] ids,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = starService.deleteStarsByArticleIds(ids,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
