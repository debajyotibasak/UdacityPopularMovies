package com.debajyotibasak.udacitypopularmovies.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<CastResult> cast;
    @SerializedName("crew")
    @Expose
    private List<CrewResult> crew;

    public CastResponse(Integer id, List<CastResult> cast, List<CrewResult> crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public List<CastResult> getCast() {
        return cast;
    }

    public List<CrewResult> getCrew() {
        return crew;
    }
}
