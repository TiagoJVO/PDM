package com.myapplication.Model.DataRepresentation.DTO;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


public class MovieInfoDTO {
    /** The Movie Title */
    @SerializedName("title")
    private String title;
    /** The Movie release date */
    @SerializedName("release_date")
    private String releaseDate;
    /** The Movie overview */
    @SerializedName("overview")
    private String overview;
    /** The Movie tagline */
    @SerializedName("tagline")
    private String tagline;
    /** The Movie rating */
    @SerializedName("vote_average")
    private String rating;
    /** The Movie genres */
    @SerializedName("genres")
    private ArrayList<GenreDTO> genres;
    /** The Movie homepage */
    @SerializedName("homepage")
    private String homepage;
    /** The Movie production companies */
    @SerializedName("production_companies")
    private ArrayList<CompanyDTO> companies;
    /** The Movie production countries */
    @SerializedName("production_countries")
    private ArrayList<CountryDTO> countries;
    /** The Movie poster path */
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("id")
    private int id;


    /** @return The Movie Title */
    public String getTitle() {
        return title;
    }

    /** @return The Movie release date */
    public String getReleaseDate() {
        return releaseDate;
    }

    /** @return The Movie overview */
    public String getOverview() {
        return overview;
    }

    /** @return The Movie tagline */
    public String getTagline() {
        return tagline;
    }

    /** @return The Movie rating */
    public String getRating() {
        return rating;
    }

    /** @return The Movie genres */
    public ArrayList<GenreDTO> getGenres() {
        return genres;
    }

    /** @return The Movie homepage */
    public String getHomepage() {
        return homepage;
    }

    /** @return The Movie production companies */
    public ArrayList<CompanyDTO> getCompanies() {
        return companies;
    }

    /** @return The Movie production countries */
    public ArrayList<CountryDTO> getCountries() {
        return countries;
    }

    /** @return The Movie poster path */
    public String getPoster_path() {
        return poster_path;
    }

    /** @return The Movie id */
    public int getId() {
        return id;
    }


    public class CountryDTO{
        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        /** The Country name */
        @SerializedName("name")
        private String name;
        /** The Contry id according to iso 3166 1 */
        @SerializedName("iso_3166_1")
        private String id;
    }




    public class GenreDTO implements Serializable {
        public int getId() {
            return id;
        }

        /** The Gender name */
        @SerializedName("name")
        private String name;
        /** The Gender id */
        @SerializedName("id")
        private int id;

        /** @return The name genre */
        public String getName(){return name;}
    }



    public class CompanyDTO {
        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        /** The Company name */
        @SerializedName("name")
        private String name;
        /** The Company id */
        @SerializedName("id")
        private int id;
    }
}
