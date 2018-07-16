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
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_VIDEOS;

@Entity(tableName = TABLE_VIDEOS,
        indices = @Index("fav_movie_id"),
        foreignKeys = @ForeignKey(
                entity = FavMovieEntity.class,
                parentColumns = "movieId",
                childColumns = "fav_movie_id",
                onDelete = CASCADE))
public class VideoEntity implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private final String id;
    @SerializedName("key")
    @Expose
    private final String key;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("site")
    @Expose
    private final String site;
    @SerializedName("type")
    @Expose
    private final String type;
    @ColumnInfo(name = "fav_movie_id")
    private final Integer favMovieId;

    public VideoEntity(@NonNull String id, String key, String name, String site, String type, Integer favMovieId) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;
        this.favMovieId = favMovieId;
    }

    @NonNull
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

    public Integer getFavMovieId() {
        return favMovieId;
    }
}
