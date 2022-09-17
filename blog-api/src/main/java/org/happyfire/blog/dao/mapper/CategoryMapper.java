package org.happyfire.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.happyfire.blog.dao.pojo.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
