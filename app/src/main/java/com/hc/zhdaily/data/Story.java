package com.hc.zhdaily.data;

import java.util.List;

/**
 * Created by Administrator on 2016-07-10.
 */
public class Story {

    /**
     * "stories":[
     *  {"images":["http:\/\/pic3.zhimg.com\/24fc0bcbe6ee6b3d440870b60ce5c002.jpg"],
     *  "type":0,
     *  "id":8476096,
     *  "ga_prefix":"062316",
     *  "title":"抛开情怀，《魔兽》电影还是能让人看到一些小惊喜"}
     * ]
     */

    private List<String> images;
    private String title;
    private int id;
    private String date=null;

    public void setImages(List<String> images){
        this.images = images;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDate(String date){this.date = date;}


   public List<String> getImages(){
       return images;
   }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }

    public String getDate(){return date;}

    @Override
    public String toString() {
        return "TopStoriesEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ga_prefix='" + "null" + '\'' +
                ", images='" + images.get(0) + '\'' +
                ", type=" + "null" +
                '}';
    }


}
