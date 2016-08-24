package com.hc.zhdaily.data;

import java.util.List;

/**
 * Created by hc on 2016/8/18.
 * http://news-at.zhihu.com/api/4/story/8693045/short-comments
 * 单个评论数据：
 * {"author":"線線",
 * "content":"啊啊啊！特别关注翻译小哥的后续啊… 看到这个八卦信息 突然把之前食人鱼鳄鱼什么的全忘在脑后了。>_<",
 * "avatar":"http:\/\/pic1.zhimg.com\/ce9be9ffcab25a509020231c023e1c04_im.jpg",
 * "time":1471509278,
 * "id":26248290,
 * "likes":0},
 */
public class Comment {

    private String author;
    private String content;
    private String avatar;
    private int time;
    private String id;
    private String likes;

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setContent(String content){ this.content = content; }
    public void setAvatar(String avatar){ this.avatar = avatar; }
    public void setTime(int time){ this.time = time; }
    public void setId(String id) {
        this.id = id;
    }
    public void setLikes(String likes){ this.likes = likes; }

    public String getAuthor(){ return author; }
    public String getContent(){ return content;}
    public String getAvatar(){ return avatar;}
    public int getTime(){return time;}
    public String getId() {
        return id;
    }
    public String getLikes(){return likes;}

}
