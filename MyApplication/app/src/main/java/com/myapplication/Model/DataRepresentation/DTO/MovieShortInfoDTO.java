package com.myapplication.Model.DataRepresentation.DTO;

import com.google.gson.annotations.SerializedName;


public class MovieShortInfoDTO {

    /** The Movie Title */
    @SerializedName("title")
    private String title;
    /** The if Movie is ment for adult */
    @SerializedName("adult")
    private boolean adult;
    /** The Movie overview */
    @SerializedName("overview")
    private String overview;
    /** The Movie release date */
    @SerializedName("release_date")
    private String releaseDate;
    /** The Movie poster path */
    @SerializedName("poster_path")
    private String poster_path;
    /** The Movie rating */
    @SerializedName("vote_average")
    private double rating;
    /** The Movie id */
    @SerializedName("id")
    private int id;


    /** @return The Movie Title */
    public String getTitle() {
        return title;
    }

    /** @return The if Movie is ment for adult */
    public boolean isAdult() {
        return adult;
    }

    /** @return The Movie overview */
    public String getOverview() {
        return overview;
    }

    /** @return The Movie release date */
    public String getReleaseDate() {
        return releaseDate;
    }

    /** @return The Movie poster path */
    public String getPoster_path() {
        return poster_path;
    }

    /** @return The Movie rating */
    public double getRating() {
        return rating;
    }

    /** @return The Movie id */
    public int getId() {
        return id;
    }

}