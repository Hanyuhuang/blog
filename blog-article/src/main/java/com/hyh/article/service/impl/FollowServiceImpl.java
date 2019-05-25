package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.mapper.ArticleMapper;
import com.hyh.article.mapper.FollowMapper;
import com.hyh.article.service.FollowService;
import com.hyh.pojo.Article;
import com.hyh.pojo.Follow;
import com.hyh.pojo.User;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int insertFollow(Follow follow) {
        follow.setTime(new Date());
        return followMapper.insert(follow);
    }

    @Override
    public int deleteFollowByArticleId(Long articleId,User user) {
        Example example = new Example(Follow.class);
        example.createCriteria().andEqualTo("articleId",articleId).andEqualTo("userId",user.getId());
        return followMapper.deleteByExample(example);
    }

    @Override
    public PageResult<ArticleVo> listFollows(Integer pageCur, Integer pageSize, User user) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 查询我的点赞列表
        Example example = new Example(Follow.class);
        example.createCriteria().andEqualTo("userId",user.getId());
        // 查询
        Page<Follow> followList = (Page<Follow>) followMapper.selectByExample(example);
        // 根据查询结果 封装文章Vo
        List<ArticleVo> result = followList.stream().map(follow -> {
            ArticleVo articleVo = new ArticleVo();
            // 查询文章
            Article article = articleMapper.selectByPrimaryKey(follow.getArticleId());
            articleVo.setArticle(article);
            articleVo.setUser(user);
            return articleVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<ArticleVo>(result.size(),result);
    }

    @Override
    public int deleteFollowsByArticleIds(Long[] ids, User user) {
        int count = 0;
        for (Long id: ids) {
            count += deleteFollowByArticleId(id,user);
        }
        return count;
    }
}
