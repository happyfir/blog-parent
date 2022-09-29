package org.happyfire.blog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.happyfire.blog.admin.mapper.CategoryMapper;
import org.happyfire.blog.admin.model.params.PageParam;
import org.happyfire.blog.admin.pojo.Category;
import org.happyfire.blog.admin.service.ArticleService;
import org.happyfire.blog.admin.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private QiniuUtils qiniuUtils;

    private  String DefaultAvatar = "http://rhzmlesrf.hb-bkt.clouddn.com/%E5%88%86%E7%B1%BB.jpg";
    @Override
    public List<Category> findCategoryByName(String name) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.like(Category::getCategoryName,name);
        List<Category> categories = categoryMapper.selectList(categoryLambdaQueryWrapper);
        return categories;
    }

    @Override
    public Result findCategories(PageParam pageParam) {
        String condition = pageParam.getQueryString();
        Page<Category> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
        LambdaQueryWrapper<Category> searchQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNoneBlank(condition)){
            searchQueryWrapper.like(Category::getCategoryName,condition)
                    .or().like(Category::getDescription,condition);
        }
        Page<Category> categoryPage = categoryMapper.selectPage(page, searchQueryWrapper);
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setList(categoryPage.getRecords());
        pageResult.setTotal(categoryPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result update(Category category) {
        categoryMapper.updateById(category);
        return Result.success(null);
    }

    /**
     * 新增分类 每个分类设置一个默认图标
     * @param category
     * @return
     */
    @Override
    public Result add(Category category) {
        category.setAvatar(DefaultAvatar);
        categoryMapper.insert(category);
        return Result.success(null);
    }

    /**
     * 删除分类 如果该分类下存在文章 该分类不能删除
     * @param id
     * @return
     */
    @Override
    public Result deleteById(Long id) {
        int articleNums = articleService.findArticlesByCategoryId(id);
        if (articleNums != 0){
            return Result.fail(-8,"该分类下存在文章，无法删除该分类");
        }
        categoryMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result uploadAvatar(MultipartFile file, String categoryId) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传文件 到七牛云
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            //将图片地址传给用户
            String url = QiniuUtils.url + fileName;
            Category category = categoryMapper.selectById(categoryId);
            if (category == null){
                return Result.fail(-2,"未找到对应分类");
            }
            category.setAvatar(url);
            categoryMapper.updateById(category);
            return Result.success(url);
        }
        return Result.fail(20001,"上传失败");
    }
}
