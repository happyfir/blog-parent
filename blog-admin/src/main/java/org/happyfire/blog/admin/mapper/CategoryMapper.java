package org.happyfire.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.happyfire.blog.admin.pojo.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}

