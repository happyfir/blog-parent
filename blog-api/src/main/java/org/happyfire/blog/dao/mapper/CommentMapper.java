package org.happyfire.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;
import org.happyfire.blog.dao.pojo.Comment;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
