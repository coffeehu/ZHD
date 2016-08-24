package com.hc.zhdaily.data;

/**
 * Created by hc on 2016/8/18.
 *
 * http://news-at.zhihu.com/api/4/story-extra/8693045
 * 获得评论数，赞数 等
 * {"long_comments":0,"popularity":474,"short_comments":60,"comments":60}
 */
public class CommentCount {
    private String long_comments;
    private String popularity;
    private String short_comments;
    private String comments;

    public void setLong_comments(String long_comments) {
        this.long_comments = long_comments;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public void setShort_comments(String short_comments) {
        this.short_comments = short_comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLong_comments() {
        return long_comments;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getShort_comments() {
        return short_comments;
    }

    public String getComments() {
        return comments;
    }

}