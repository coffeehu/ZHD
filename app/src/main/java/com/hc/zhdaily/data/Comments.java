package com.hc.zhdaily.data;

import java.util.List;

/**
 * Created by hc on 2016/8/18.
 * 评论数据： comments 里有很多 comment
 * http://news-at.zhihu.com/api/4/story/8693045/short-comments
 * {"comments":[{"author":"線線","content":"啊啊啊！特别关注翻译小哥的后
 */
public class Comments {
    private List<Comment> comments;
    public void setComments(List<Comment> comments){ this.comments = comments;}
    public List<Comment> getComments(){ return comments; }
}
