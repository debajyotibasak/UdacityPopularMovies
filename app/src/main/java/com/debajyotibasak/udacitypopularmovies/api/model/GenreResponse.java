package com.debajyotibasak.udacitypopularmovies.api.model;

import com.debajyotibasak.udacitypopularmovies.database.entity.GenreEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreResponse {

    @SerializedName("genres")
    @Expose
    private List<GenreEntity> genres = null;

    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }
}
