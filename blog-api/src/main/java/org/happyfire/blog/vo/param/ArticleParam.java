package org.happyfire.blog.vo.param;

import lombok.Data;
import org.happyfire.blog.vo.CategoryVo;
import org.happyfire.blog.vo.TagVo;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
