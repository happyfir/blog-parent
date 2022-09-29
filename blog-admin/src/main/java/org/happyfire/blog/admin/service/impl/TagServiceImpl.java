package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.ArticleTagMapper;
import org.happyfire.blog.admin.mapper.TagMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.ArticleTag;
import org.happyfire.blog.admin.pojo.Tag;
import org.happyfire.blog.admin.service.TagService;
import org.happyfire.blog.admin.utils.QiniuUtils;
import org.happyfire.blog.admin.vo.PageResult;
import org.happyfire.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private QiniuUtils qiniuUtils;

    private  String DefaultAvatar = "http://rhzmlesrf.hb-bkt.clouddn.com/300.jpg";
    @Override
    public List<Tag> findTagsByArticleName(String name) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Tag::getTagName,name);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return tagList;
    }

    @Override
    public Result findTags(PageParam pageParam) {
        String condition = pageParam.getQueryString();
        Page<Tag> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(condition)){
            lambdaQueryWrapper.like(Tag::getTagName,condition);
        }
        Page<Tag> tagPage = tagMapper.selectPage(page, lambdaQueryWrapper);
        PageResult<Tag> pageResult = new PageResult<>();
        pageResult.setList(tagPage.getRecords());
        pageResult.setTotal(tagPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result update(Tag tag) {
        tagMapper.updateById(tag);
        return Result.success(null);
    }

    @Override
    public Result add(Tag tag) {
        tag.setAvatar(DefaultAvatar);
        tagMapper.insert(tag);
        return Result.success(null);
    }

    /**
     * 删除标签 如果还有文章属于该标签 则不能删除
     * @param id
     * @return
     */
    @Override
    public Result deleteById(Long id) {
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getTagId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(lambdaQueryWrapper);
        if (articleTags.size() != 0){
            return Result.fail(-8,"该标签下存在文章，无法删除该标签");
        }
        tagMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result uploadAvatar(MultipartFile file, String tagId) {
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传文件 到七牛云
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            String url = QiniuUtils.url + fileName;
            Tag tag = tagMapper.selectById(tagId);
            if (tag == null){
                return Result.fail(-2,"未找到对应标签");
            }
            tag.setAvatar(url);
            tagMapper.updateById(tag);
            return Result.success(url);
        }
        return Result.fail(20001,"上传失败");
    }
}
