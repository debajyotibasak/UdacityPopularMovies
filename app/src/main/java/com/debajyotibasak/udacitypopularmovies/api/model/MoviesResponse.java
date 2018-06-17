package com.debajyotibasak.udacitypopularmovies.api.model;

import android.arch.persistence.room.Ignore;

import com.debajyotibasak.udacitypopularmovies.database.entity.MovieEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    @Ignore
    private List<MovieEntity> results;

    public MoviesResponse(Integer page, Integer totalResults, Integer totalPages, List<MovieEntity> results) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<MovieEntity> getResults() {
        return results;
    }
}
