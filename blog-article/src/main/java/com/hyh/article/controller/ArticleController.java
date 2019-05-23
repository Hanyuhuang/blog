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

@CrossOrigin
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
            // 添加文章
            int result = articleService.saveArticle(article);
            if (result <1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    @GetMapping("/list")
    public ResponseEntity<PageResult<Article>> listArticlesByPage(Integer pageCur,Integer pageSize,
                                            String orderBy,Boolean desc,String keywords){
        try {
            PageResult<Article> result = articleService.listArticlesByPage(pageCur,pageSize,orderBy,desc,keywords);
            if (result.getItems().size()<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 查询文章详情
     * @return
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<ArticleVo> getArticleDetail(@PathVariable("id") Long id){
        try {
            ArticleVo result = articleService.getArticleDetail(id);
            if (result==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 查询最近文章
     * @return
     */
    @GetMapping("/recent")
    public ResponseEntity<PageResult<ArticleVo>> listRecentArticles(){
        try {
            PageResult<ArticleVo> result = articleService.listRecentArticles();
            if (result.getItems().size()<1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
