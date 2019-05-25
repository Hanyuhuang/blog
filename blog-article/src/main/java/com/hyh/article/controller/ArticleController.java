package com.hyh.article.controller;

import com.hyh.article.service.ArticleService;
import com.hyh.pojo.Article;
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
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 写文章
     * @param article
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<Integer> insertArticle(@RequestBody Article article, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            // 添加文章
            article.setUserId(user.getId());
            int result = articleService.saveArticle(article);
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 分页查询文章
     * @param pageCur  当前页
     * @param pageSize 每页数量
     * @param orderBy  按照某个字段排序
     * @param desc     是否降序
     * @param keywords 搜索关键字
     * @return
     */
    @GetMapping("/myArticle")
    public ResponseEntity<PageResult<Article>> listMyArticles(Integer pageCur,Integer pageSize,
                                            String orderBy,Boolean desc,String keywords,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageResult<Article> result = articleService.listMyArticles(pageCur,pageSize,orderBy,desc,keywords,user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询文章详情
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<ArticleVo> getArticleDetail(@PathVariable("id") Long id,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 查询文章详情
            ArticleVo result = articleService.getArticleDetail(id,user);
            if (result==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询最近文章
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<PageResult<ArticleVo>> listArticles(Integer pageCur,Integer pageSize){
        try {
            PageResult<ArticleVo> result = articleService.listArticles(pageCur,pageSize);
            if (result.getItems().size()<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hot")
    public ResponseEntity<PageResult<Article>> listHotArticles(){
        try {
            PageResult<Article> result = articleService.listHotArticles();
            if (result.getItems().size()<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 根据文章id查询文章
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable("id") Long id){
        try {

            Article result = articleService.getArticleById(id);
            if (result == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 根据文章id删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> listHotArticles(@PathVariable("id") Long id){
        try {
            int result = articleService.deleteArticleById(id);
            if (result <1 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改文章
     * @param article
     * @param session
     * @return
     */
    @PutMapping
    public ResponseEntity<Integer> updateArticle(@RequestBody Article article,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            // 用户未登录
            if (user==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            int result = articleService.updateArticle(article);
            if (result <1 ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
