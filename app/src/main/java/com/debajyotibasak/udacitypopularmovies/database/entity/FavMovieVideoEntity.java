package com.debajyotibasak.udacitypopularmovies.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_REVIEWS;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_VIDEOS;

@Entity(tableName = TABLE_VIDEOS,
        foreignKeys = @ForeignKey(
                entity = FavMovieEntity.class,
                parentColumns = "movieId",
                childColumns = "favMovieId",
                onDelete = CASCADE))
public class FavMovieVideoEntity implements Serializable {
    @PrimaryKey
    private final String id;
    private final String key;
    private final String name;
    private final String site;
    private final String type;
    private final int favMovieId;

    public FavMovieVideoEntity(String id, String key, String name, String site, String type, int favMovieId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.favMovieId = favMovieId;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public int getFavMovieId() {
        return favMovieId;
    }
}
