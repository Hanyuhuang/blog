package com.hyh.article.service;

import com.hyh.pojo.Article;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;

public interface ArticleService {

    int saveArticle(Article article);

    PageResult<Article> listArticlesByPage(Integer pageCur, Integer pageSize, String orderBy, Boolean desc, String keywords);

    PageResult<ArticleVo> listRecentArticles();

    PageResult<Article> listHotArticles();

    ArticleVo getArticleDetail(Long id);
}
