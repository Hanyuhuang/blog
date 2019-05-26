package com.hyh.article.service;

import com.hyh.pojo.Bo.FollowBo;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;

public interface FollowService {

    int insertFollow(FollowBo followBo,User user);

    int deleteFollowByArticleId(Long articleId,User user);

    PageResult<ArticleVo> listFollows(Integer pageCur, Integer pageSize, User user);

    int deleteFollowsByArticleIds(Long[] ids, User user);
}
