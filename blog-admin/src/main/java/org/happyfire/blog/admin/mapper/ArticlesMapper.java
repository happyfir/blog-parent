package org.happyfire.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.happyfire.blog.admin.pojo.Article;

@Mapper
public interface ArticlesMapper extends BaseMapper<Article> {
}
