package com.hc.zhdaily.data;

/**
 * Created by Administrator on 2016-07-10.
 */
public class ThemeContent extends ListContent {

    /**
     * http://news-at.zhihu.com/api/4/theme/3 （2-13） (id 可由 http://news-at.zhihu.com/api/4/themes 的 others 取得)
     *
     * 有些有图片，有些没图片
     *{"
     *  stories":[   ------> 映射为 Story 的集合
     *     {"type":0,"id":7483361,"title":"更多电影内容，都在读读日报里"},
     *     {"type":0,"id":7400530,"title":"哪些电影有浓厚的建筑人文意味？"},
     *     {"images":["http:\/\/pic4.zhimg.com\/e43e1f7d8dfce546c4e95e944bcfb6cb_t.jpg"],"type":2,"id":7260659,"title":"「新片」十月份，值得你走进影院的电影总会有一些"},
     *     {"images":["http:\/\/pic4.zhimg.com\/d57c509391f2d2b9e50bdf6696f9b01f.jpg"],"type":1,"id":7249394,"title":"是不是发现自己除了久石让，其他的日本配乐师都不认识…"},
     *     {"images":["http:\/\/pic1.zhimg.com\/48e16ae19f3955bac6e5ef890102d210_t.jpg"],"type":1,"id":7242546,"title":"知道这些彩蛋，《头脑特工队》将好看10倍"},
     *     {"images":["http:\/\/pic3.zhimg.com\/42a7dd7cf12b3fb5903d78273dfc6c8e_t.jpg"],"type":1,"id":7090984,"title":"玩物 | 万军丛中过，滴血不沾身，刺客的信条——袖刃"}
     *     ],
     * "description":"除了经典和新片，我们还关注技术和产业",
     * "background":"http:\/\/p1.zhimg.com\/80\/0b\/800b79a4821a535de31b349ffdc9eabb.jpg",
     * "color":14483535,"name":"电影日报",
     * "image":"http:\/\/p1.zhimg.com\/dd\/f1\/ddf10a04227ea50fd59746dbcd13c728.jpg",
     * "editors":[{"url":"http:\/\/www.zhihu.com\/people\/deng-ruo-xu","bio":"好奇心日报","id":82,"avatar":"http:\/\/pic2.zhimg.com\/d3b31fa32_m.jpg","name":"邓若虚"},{"url":"http:\/\/www.zhihu.com\/people\/yu-ke-er","bio":"电影产业研究者","id":40,"avatar":"http:\/\/pic4.zhimg.com\/9d9e1f217_m.jpg","name":"余柯儿"},{"url":"http:\/\/www.zhihu.com\/people\/wo-jiao-a-lang","bio":"《看电影》杂志主编","id":85,"avatar":"http:\/\/pic3.zhimg.com\/10b09d8b79a5315273824d310c4d602e_m.jpg","name":"我叫阿郎"},{"url":"http:\/\/www.zhihu.com\/people\/zhao-shu-rong","bio":"《看电影》杂志编辑","id":84,"avatar":"http:\/\/pic1.zhimg.com\/57c7b29e4_m.jpg","name":"赵恕容"}],
     * "image_source":""
     * }
     */

//    private List<Story> stories;
    private String description;
    private String background;
    private String name;
    private String image;

    //public void setStories(List<Story> stories){this.stories = stories;}

    public void setDescription(String description){
        this.description = description;
    }

    public void setBackground(String background){
        this.background = background;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImage(String image){
        this.image = image;
    }


    //public List<Story> getStories(){return stories;}

    public String getDescription(){
        return description;
    }

    public String getBackground(){
        return background;
    }

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

}
