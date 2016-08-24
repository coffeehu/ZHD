package com.hc.zhdaily.data;

import java.util.List;

/**
 * Created by Administrator on 2016-07-14.
 */
public class TopStory {
    /**
     * "top_stories":[
     * {"image":"http:\/\/pic3.zhimg.com\/004defd2dfb8eab34988eacb8e44feaa.jpg","type":0,"id":8553080,"ga_prefix":"071409","title":"我的工作是照顾 100 头在草原飞跑的神兽"},
     * {"image":"http:\/\/pic1.zhimg.com\/c3e283f1e50215ab821d25c426aba7fc.jpg","type":0,"id":8562326,"ga_prefix":"071407","title":"作为妇产科男医生，我来说说卫生棉条"},
     * {"image":"http:\/\/pic4.zhimg.com\/3aa8554c9ad14767ee3ca96b2f6d04cb.jpg","type":0,"id":8561358,"ga_prefix":"071407","title":"最近刷屏的「葛优躺」，背后的故事还要说回 23 年前"}
     * ]
     */

    private String image;
    private String title;
    private int id;

    public void setImages(String image){
        this.image = image;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getImage(){
        return image;
    }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }

}
