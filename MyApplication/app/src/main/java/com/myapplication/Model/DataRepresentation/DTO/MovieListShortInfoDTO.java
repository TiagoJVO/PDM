package com.myapplication.Model.DataRepresentation.DTO;


import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Class that defines the DTO (Data Transfer Object) that contains List of Movie Basic information data
 * obtained from the The Movie Database Service API.
 *
 * <p>The class does not contain all the fields produced by the API. It merely contains those
 * that are actually used. Deserialization is performed according to GSON's conventions.</p>
 *
 * Design notes: Suppress warnings; class created just because GSON converter can't convert an list in json to a List<MovieShortDTO> directly
 */
public class MovieListShortInfoDTO {


    /** The MovieShort List*/
    @SerializedName("results")
    private List<MovieShortInfoDTO> list;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int numberOfPages;


    /** @return The MovieShort List*/
    public List<MovieShortInfoDTO> getList(){return list;}

    /** @return The Number of Pages*/
    public int getNumberOfPages(){return numberOfPages;}

    /** @return The Page*/
    public int getPage(){return page;}

}
