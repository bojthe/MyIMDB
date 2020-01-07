package com.example.moviedb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("backdrops")
    @Expose
    private List<ImageResult> backdrops = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ImageResult> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<ImageResult> backdrops) {
        this.backdrops = backdrops;
    }
}
