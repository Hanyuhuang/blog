package com.hyh.article.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hyh.article.mapper.ArticleMapper;
import com.hyh.article.mapper.ViewMapper;
import com.hyh.article.service.ViewService;
import com.hyh.pojo.Article;
import com.hyh.pojo.User;
import com.hyh.pojo.View;
import com.hyh.pojo.Vo.ArticleVo;
import com.hyh.pojo.Vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViewServiceImpl implements ViewService {

    @Autowired
    private ViewMapper viewMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public View getViewByArticle(Long articleId, Long userId) {
        Example example = new Example(View.class);
        example.createCriteria().andEqualTo("articleId",articleId).andEqualTo("userId",userId);
        return viewMapper.selectOneByExample(example);
    }

    @Override
    public int insertView(View view) {
        return viewMapper.insert(view);
    }

    @Override
    public int updateView(View view) {
        return viewMapper.updateByPrimaryKeySelective(view);
    }

    @Override
    public int deleteViewById(Long id) {
        View view = new View();
        view.setId(id);
        view.setStatus(0);
        return viewMapper.updateByPrimaryKeySelective(view);
    }

    @Override
    public int deleteViewsById(Long[] ids) {
        int count = 0;
        for (Long id: ids ) {
            count += deleteViewById(id);
        }
        return count;
    }

    @Override
    public int deleteAllViews(Long id) {
        Example example = new Example(View.class);
        example.createCriteria().andEqualTo("userId",id);
        List<View> list = viewMapper.selectByExample(example);
        int count = 0;
        for (View view: list) {
            count += deleteViewById(view.getId());
        }
        return count;
    }

    @Override
    public PageResult<ArticleVo> listViews(Integer pageCur, Integer pageSize, User user) {
        // 开始分页
        PageHelper.startPage(pageCur, pageSize);
        // 查询我浏览的记录 并且状态为1的
        Example example = new Example(View.class);
        example.createCriteria().andEqualTo("userId",user.getId()).andEqualTo("status",1);
        // 查询
        Page<View> viewList = (Page<View>) viewMapper.selectByExample(example);
        // 根据查询结果 封装文章Vo
        List<ArticleVo> result = viewList.stream().map(view -> {
            ArticleVo articleVo = new ArticleVo();
            // 查询文章
            Article article = articleMapper.selectByPrimaryKey(view.getArticleId());
            articleVo.setArticle(article);
            articleVo.setUser(user);
            return articleVo;
        }).collect(Collectors.toList());
        // 返回结果
        return new PageResult<ArticleVo>(result.size(),result);
    }
}
