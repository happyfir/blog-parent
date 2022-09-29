package org.happyfire.blog.admin.service;

import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Tag;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TagService {
    List<Tag> findTagsByArticleName(String condition);

    Result findTags(PageParam pageParam);

    Result update(Tag tag);

    Result add(Tag tag);

    Result deleteById(Long id);

    Result uploadAvatar(MultipartFile file, String tagId);
}
