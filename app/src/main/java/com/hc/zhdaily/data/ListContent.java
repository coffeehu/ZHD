package com.hc.zhdaily.data;

import com.hc.zhdaily.data.Story;

import java.util.List;

/**
 * Created by Administrator on 2016-07-12.
 */
public class ListContent {

    private List<Story> stories;

    public void setStories(List<Story> stories){
        this.stories = stories;
    }

    public List<Story> getStories(){
        return stories;
    }
}
