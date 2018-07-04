package com.debajyotibasak.udacitypopularmovies.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.debajyotibasak.udacitypopularmovies.database.converter.IntegerListConvertor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_FAVORITE_MOVIES;
import static com.debajyotibasak.udacitypopularmovies.utils.AppConstants.TABLE_MOVIES;

@Entity(tableName = TABLE_FAVORITE_MOVIES)
public class FavMovieEntity implements Serializable {

    @PrimaryKey
    private final int movieId;
    private final int voteCount;
    private final double voteAverage;
    private final String title;
    private final String posterPath;
    @TypeConverters(IntegerListConvertor.class)
    private final List<Integer> genreIds;
    private final String backdropPath;
    private final String overview;
    private final String releaseDate;
    @TypeConverters(IntegerListConvertor.class)
    private final List<Integer> castIds;
    private final long createdAt;

    public FavMovieEntity(final int movieId, final int voteCount, final double voteAverage, String title, String posterPath,
                          final List<Integer> genreIds, String backdropPath, String overview, String releaseDate,
                          final List<Integer> castIds, final long createdAt) {
        this.movieId = movieId;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.castIds = castIds;
        this.createdAt = createdAt;
    }

    public int getMovieId() {
        return movieId;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Integer> getCastIds() {
        return castIds;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
