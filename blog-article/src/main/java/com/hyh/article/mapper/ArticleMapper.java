package com.hyh.article.mapper;

import com.hyh.pojo.Article;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface ArticleMapper extends Mapper<Article> {

    @Select("select title from article where id = #{id}")
    String getArticleTitleById(Long id);

}
