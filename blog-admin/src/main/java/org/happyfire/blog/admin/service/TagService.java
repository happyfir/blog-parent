package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.pojo.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findTagsByArticleName(String condition);
}
