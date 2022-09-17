package org.happyfire.blog.service;

import org.happyfire.blog.vo.Result;
import org.happyfire.blog.vo.param.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id 查询所有评论
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);

    /**
     * 评论功能
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
