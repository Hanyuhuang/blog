package com.hyh.article.service;

import com.hyh.pojo.Article;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;

public interface ArticleService {

    int saveArticle(Article article);

    PageResult<Article> listMyArticles(Integer pageCur, Integer pageSize, String orderBy, Boolean desc, String keywords);

    PageResult<ArticleVo> listArticles(Integer pageCur,Integer pageSize);

    PageResult<Article> listHotArticles();

    ArticleVo getArticleDetail(Long id);

    int deleteArticleById(Long id);

    int updateArticle(Article article);

    Article getArticleById(Long id);
}
