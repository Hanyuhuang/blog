package com.hyh.article.service;

import com.hyh.pojo.Star;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;

public interface StarService {

    int insertStar(Star star);

    int deleteStarByArticleId(Long articleId,User user);

    PageResult<ArticleVo> listStars(Integer pageCur, Integer pageSize, User user);

    int deleteStarsByArticleIds(Long[] ids, User user);
}
