package com.hyh.article.service;

import com.hyh.pojo.Article;
import com.hyh.pojo.User;
import com.hyh.pojo.View;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;

public interface ViewService {

    View getViewByArticle(Long articleId, Long userId);

    int insertView(View view);

    int updateView(View view);

    int deleteViewByArticleId(Long articleId,User user);

    int deleteViewsByArticleId(Long[] ids,User user);

    int deleteAllViews(User user);

    PageResult<ArticleVo> listViews(Integer pageCur, Integer pageSize, User user);
}
