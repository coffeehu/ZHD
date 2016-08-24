package com.hc.zhdaily.data;

/**
 * Created by Administrator on 2016-07-10.
 */
public class MenuData {

    /**
     * http://news-at.zhihu.com/api/4/themes 的 others
     *
     * "others":
     *     [
     *      {"color":15007,"thumbnail":"http:\/\/pic3.zhimg.com\/0e71e90fd6be47630399d63c58beebfc.jpg","description":"了解自己和别人，了解彼此的欲望和局限。","id":13,"name":"日常心理学"},
     *      {"color":8307764,"thumbnail":"http:\/\/pic4.zhimg.com\/2c38a96e84b5cc8331a901920a87ea71.jpg","description":"内容由知乎用户推荐，海纳主题百万，趣味上天入地","id":12,"name":"用户推荐日报"},
     *      {"color":14483535,"thumbnail":"http:\/\/pic3.zhimg.com\/00eba01080138a5ac861d581a64ff9bd.jpg","description":"除了经典和新片，我们还关注技术和产业","id":3,"name":"电影日报"},
     *      {"color":1564695,"thumbnail":"http:\/\/pic4.zhimg.com\/eac535117ed895983bd2721f35d6e8dc.jpg","description":"有音乐就很好","id":7,"name":"音乐日报"},
     *      {"color":6123007,"thumbnail":"http:\/\/pic1.zhimg.com\/a0f97c523c64e749c700b2ddc96cfd7c.jpg","description":"用技术的眼睛仔细看懂每一部动画和漫画","id":9,"name":"动漫日报"},
     *      {"color":16046124,"thumbnail":"http:\/\/pic1.zhimg.com\/bcf7d594f126e5ceb22691be32b2650a.jpg","description":"关注体育，不吵架。","id":8,"name":"体育日报"}
     *     ]
     */

    private String thumbnail;
    private String description;
    private int id;
    private String name;

    public void setThumbnail(String thumbnail){
        this.thumbnail = thumbnail;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }


    public String getThumbnail(){
        return thumbnail;
    }

    public String getDescription(){
        return description;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

}
