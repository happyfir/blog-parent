package org.happyfire.blog.admin.vo;

import lombok.Data;
import org.happyfire.blog.admin.pojo.Category;
import org.happyfire.blog.admin.pojo.Tag;

import java.util.List;

@Data
public class ArticleVo {

    private String id;

    private String authorId;
    /**
     * 作者账户
     */
    private String account;
    /**
     * 作者昵称
     */
    private String nickName;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     * 创建时间
     */
    private String createDate;


    private String tag;

    private Category category;
}

