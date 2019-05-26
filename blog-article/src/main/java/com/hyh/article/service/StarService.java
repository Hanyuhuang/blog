package com.hyh.article.service;

import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import com.hyh.pojo.Bo.StarBo;

public interface StarService {

    int insertStar(StarBo starBo, User user);

    int deleteStarByArticleId(Long articleId,User user);

    PageResult<ArticleVo> listStars(Integer pageCur, Integer pageSize, User user);

    int deleteStarsByArticleIds(Long[] ids, User user);
}
