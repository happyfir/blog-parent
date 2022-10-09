package org.happyfire.blog.vo.param;

import lombok.Data;

@Data
public class PageParams {
    private int page = 1;
    private int pageSize = 5;
    private Long categoryId;
    private Long tagId;
    private String year;
    private String condition;
    private String month;
    private Long authorId;
    public String getMonth(){
        if (this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }
        return this.month;
    }
}
