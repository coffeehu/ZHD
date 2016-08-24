package com.hc.zhdaily.data;

import com.hc.zhdaily.data.ListContent;
import com.hc.zhdaily.data.TopStory;

import java.util.List;

/**
 * Created by Administrator on 2016-07-12.
 */
public class Latest extends ListContent {

    /**
     * http://news-at.zhihu.com/api/4/news/latest
     *
     * {    "date":"20160712",
     *      "stories":[
     *          {"images":["http:\/\/pic4.zhimg.com\/3eb29832d01e609bfcbad47774c5406b.jpg"],"type":0,"id":8557565,"ga_prefix":"071221","title":"另一个层面的烧脑神片，关于钱的，想明白透不容易"},
     *          {"images":["http:\/\/pic2.zhimg.com\/467d67351e0f11e158a385456b0d5635.jpg"],"type":0,"id":8542522,"ga_prefix":"071220","title":"想改造自己的性格倒是可以，不过慢慢来吧"}
     *          ],
     *      "top_stories":[
     *          {"image":"http:\/\/pic1.zhimg.com\/92165eacf4dbf25af097a51569a67d60.jpg","type":0,"id":8557565,"ga_prefix":"071221","title":"另一个层面的烧脑神片，关于钱的，想明白透不容易"},
     *          {"image":"http:\/\/pic1.zhimg.com\/b08aa9e479ee2ad5488b2acdc1e1ad80.jpg","type":0,"id":8552429,"ga_prefix":"071217","title":"知乎好问题 · 有哪些很重要却一直鲜为人知的事？"}
     *          ]
     * }
     */

    private List<TopStory> top_stories;
   // private List<Story> stories;
    private String date;

   public void setTop_stories(List<TopStory> top_stories){
        this.top_stories = top_stories;
    }

    //public void setStories(List<Story> stories){this.stories = stories;}

    public void setDate(String date){
        this.date = date;
    }


    public List<TopStory> getTop_stories(){
        return top_stories;
    }

    //public List<Story> getStories(){return stories;}

    public String getDate(){
        return date;
    }

}
