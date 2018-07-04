package com.debajyotibasak.udacitypopularmovies.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_CAST;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_FAVORITE_MOVIES;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_MOVIES;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_REVIEWS;

@Entity(tableName = TABLE_REVIEWS,
        foreignKeys = @ForeignKey(
                entity = FavMovieEntity.class,
                parentColumns = "movieId",
                childColumns = "favMovieId",
                onDelete = CASCADE))
public class FavMovieReviewEntity implements Serializable{

    @PrimaryKey
    private final String id;
    private final String author;
    private final String content;
    private final String url;
    private final int favMovieId;

    public FavMovieReviewEntity(String author, String content, String id, String url, final int favMovieId) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.favMovieId = favMovieId;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public int getFavMovieId() {
        return favMovieId;
    }
}
