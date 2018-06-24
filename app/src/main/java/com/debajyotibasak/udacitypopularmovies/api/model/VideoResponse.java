package com.debajyotibasak.udacitypopularmovies.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoResults> results;

    public VideoResponse(Integer id, List<VideoResults> results) {
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public List<VideoResults> getResults() {
        return results;
    }
}
