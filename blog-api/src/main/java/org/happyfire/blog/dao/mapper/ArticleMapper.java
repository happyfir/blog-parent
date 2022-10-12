package org.happyfire.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.happyfire.blog.dao.dos.Archives;
import org.happyfire.blog.dao.pojo.Article;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month,
                               Long authorId);

    /**
     * 获取所有文章
     * @return
     */
    List<Article> listArticles();
}
