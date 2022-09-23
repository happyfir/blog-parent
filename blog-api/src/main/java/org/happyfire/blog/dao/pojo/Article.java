package org.happyfire.blog.dao.pojo;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer  viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight = Article_Common;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 是否删除
     * 1 代表删除  0 代表未删除
     */
    private Integer deleted = 0;
}
