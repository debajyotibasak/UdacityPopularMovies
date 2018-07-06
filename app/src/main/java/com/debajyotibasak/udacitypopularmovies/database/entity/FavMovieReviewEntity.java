package com.debajyotibasak.udacitypopularmovies.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_CAST;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_FAVORITE_MOVIES;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_MOVIES;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_REVIEWS;

@Entity(tableName = TABLE_REVIEWS,
        indices = @Index("fav_movie_id"),
        foreignKeys = @ForeignKey(
                entity = FavMovieEntity.class,
                parentColumns = "movieId",
                childColumns = "fav_movie_id",
                onDelete = CASCADE))
public class FavMovieReviewEntity implements Serializable{

    @PrimaryKey
    @NonNull
    private final String id;
    private final String author;
    private final String content;
    private final String url;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public FavMovieReviewEntity(String author, String content, @NonNull String id, String url, final Integer favMovieId) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.favMovieId = favMovieId;
    }

    @NonNull
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

    public Integer getFavMovieId() {
        return favMovieId;
    }
}
