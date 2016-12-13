package com.myapplication.Model.DataRepresentation;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;


public class MovieInfo implements Parcelable{
    private final String title;
    private final String releaseDate;
    private final String overview;
    private final String tagline;
    private final String rating;
    private final ArrayList<Gender> genres;
    private final String homepage;
    private final ArrayList<Company> companies;
    private final ArrayList<Country> countries;
    private final String poster_path;
    private final int id;

    public MovieInfo(String title, String releaseDate, String overview, String tagline, String rating, ArrayList<Gender> genres, String homepage, ArrayList<Company> companies, ArrayList<Country> countries, String poster_path, int id) {

        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.tagline = tagline;
        this.rating = rating;
        this.genres = genres;
        this.homepage = homepage;
        this.companies = companies;
        this.countries = countries;
        this.poster_path = poster_path;
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getTagline() {
        return tagline;
    }

    public String getRating() {
        return rating;
    }

    public String getGenres() {
        if(genres.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(genres.get(0).name);
        sb.append("\t");

        for (int i = 1; i<genres.size();++i)
        {
            sb.append(",");
            sb.append(genres.get(i).name);
            sb.append("\t");
        }

        return sb.toString();
    }

    public String getHomepage() {
        return homepage;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public ArrayList<Gender> getGenderList() {
        return genres;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(tagline);
        dest.writeString(rating);
        dest.writeList(genres);
        dest.writeString(poster_path);
        dest.writeInt(id);
        dest.writeString(homepage);
        dest.writeList(companies);
        dest.writeList(countries);
    }

    /** initiate a MovieInfo from a Parcel value*/
    public MovieInfo(Parcel in){

        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        tagline = in.readString();
        rating = in.readString();
        genres = in.readArrayList(MovieInfo.class.getClassLoader());
        poster_path = in.readString();
        id = in.readInt();
        homepage = in.readString();
        companies = in.readArrayList(MovieInfo.class.getClassLoader());
        countries = in.readArrayList(MovieInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public static class Company implements Serializable{
        private final int id;
        private final String name;

        public Company(int id, String name) {

            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Gender implements Serializable {
        private final int id;
        private final String name;

        public Gender(int id, String name) {

            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Country implements Serializable{
        private final String id;
        private final String name;

        public Country(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
