package com.debajyotibasak.udacitypopularmovies.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_CAST;

@Entity(tableName = TABLE_CAST)
public class FavMovieCastEntity implements Serializable{

    @PrimaryKey
    @NonNull
    private final Integer id;
    private String name;
    private String profilePath;

    public FavMovieCastEntity(@NonNull final Integer id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
