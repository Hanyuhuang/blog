package com.hyh.article.controller;


import com.hyh.article.service.ViewService;
import com.hyh.pojo.User;
import com.hyh.pojo.View;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@CrossOrigin(value = "http://localhost:8080",allowCredentials = "true")
@RestController
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private ViewService viewService;


    /**
     * 分页查询文章
     * @param pageCur  当前页
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<PageResult<ArticleVo>> listViews(Integer pageCur, Integer pageSize, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageResult<ArticleVo> result = viewService.listViews(pageCur,pageSize,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 添加浏览记录
     * @param articleId
     * @param session
     * @return
     */
    @PostMapping("/{id}")
    public ResponseEntity<Integer> insertView(@PathVariable("id") Long articleId,HttpSession session){
        try {
            // 添加浏览记录
            User user = (User) session.getAttribute("user");
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            View view = viewService.getViewByArticle(articleId,user.getId());
            int result = 0;
            // 第一次浏览 添加浏览记录
            if (view==null){
                view = new View(user.getId(),articleId,1,new Date());
                result = viewService.insertView(view);
            } else {
                // 从浏览记录中删除 重新修改状态
                if (view.getStatus()==0) {
                    view.setStatus(1);
                    // 再次浏览 更新时间
                }else {
                    view.setUpdateTime(new Date());
                }
                result = viewService.updateView(view);
            }
            if (result<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 根据id删除浏览记录
     * @param articleId
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteViewByArticleId(@PathVariable("id") Long articleId,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            int result = viewService.deleteViewByArticleId(articleId,user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据id批量删除浏览记录
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Integer> deleteViewsByArticleId(Long[] ids,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            int result = viewService.deleteViewsByArticleId(ids,user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除所有浏览记录
     * @param session
     * @return
     */
    @DeleteMapping("/all")
    public ResponseEntity<Integer> deleteAllViews(HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = viewService.deleteAllViews(user);
            if (result < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
