package com.myapplication.Model.DataRepresentation;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieShortInfo implements Parcelable {

    private final String title;
    private final boolean isAdult;
    private final String overview;
    //public String releaseDate; //para teste dos follow service
    private final String releaseDate;
    private final String poster_path;
    private final double rating;
    private final int id;

    public MovieShortInfo(String title, boolean isAdult, String overview, String releaseDate, String poster_path, double rating, int id) {
        this.title = title;
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.poster_path = poster_path;
        this.rating = rating;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeBooleanArray(new boolean[]{isAdult});
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(poster_path);
        dest.writeDouble(rating);
        dest.writeInt(id);
    }

    /** initiate a MovieShortDTO from a Parcel value*/
    public MovieShortInfo(Parcel in){

        title = in.readString();
        boolean[] aux = new boolean[1];
        in.readBooleanArray(aux);
        isAdult = aux[0];
        overview = in.readString();
        releaseDate = in.readString();
        poster_path = in.readString();
        rating = in.readDouble();
        id = in.readInt();
    }

    public final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieShortInfo createFromParcel(Parcel in) {
            return new MovieShortInfo(in);
        }

        public MovieShortInfo[] newArray(int size) {
            return new MovieShortInfo[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public double getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

}