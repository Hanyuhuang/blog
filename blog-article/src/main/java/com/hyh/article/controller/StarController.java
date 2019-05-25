package com.hyh.article.controller;

import com.hyh.article.service.StarService;
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
            if (result.getItems().size()<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 点赞
     * @param star
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<Integer> insertStar(@RequestBody Star star, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            star.setUserId(user.getId());
            int result = starService.insertStar(star);
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
    public ResponseEntity<Integer> deleteStarByArticleId(@PathVariable("id") Long articleId,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = starService.deleteStarByArticleId(articleId,user);
            if (result <1 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            if (result <1 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
