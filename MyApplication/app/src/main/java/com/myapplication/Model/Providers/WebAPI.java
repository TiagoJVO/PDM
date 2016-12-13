package com.myapplication.Model.Providers;

import com.myapplication.Model.DataRepresentation.DTO.MovieInfoDTO;
import com.myapplication.Model.DataRepresentation.DTO.MovieListShortInfoDTO;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface WebAPI {

    /** The base URL of the Weather Service API. */
    String BASE_URL = "https://api.themoviedb.org";

    /** The application's API key. */
    String API_KEY = "19ff0f3620c94244c8ecb64e7f1d197a";


    /**
     *<p>
     * Gets the movie information for given id and language. In case of the given language isn't
     * available for some field the default language is used instead. Corresponds to HTTP requests
     * in the form:
     *
     * GET {@literal https://api.themoviedb.org/3/movie/{id}?api_key=apiKey}
     *</p>
     *
     * @param id
     *          The Movie id
     * @param apiKey
     *          The application's API key.
     * @param language
     *          The language to be used in the query (e.g. pt, en).

     * @return The information of the Movie by the given id.
     */
    @GET("/3/movie/{id}")
    Call<MovieInfoDTO> getMovie( @Path("id") int id,
                             @Query("api_key") String apiKey,
                             @Query("language") String language);

    /**
     *<p>
     * Gets a list movie basic information for given search string, language and page. In case of the given language isn't
     * available for some field the default language is used instead. Corresponds to HTTP requests
     * in the form:
     *
     * GET {@literal https://api.themoviedb.org/3/search/movie?api_key=apiKey&query=query}
     *</p>
     *
     * @param apiKey
     *          The application's API key.
     * @param query
     *          The query string to search for.
     * @param page
     *          The result page wanted.
     * @param language
     *          The language to be used in the query (e.g. pt, en).

     * @return A object that contains a List os movie basic information searched.
     */
    @GET("/3/search/movie")
    Call<MovieListShortInfoDTO> getMovieSearch(@Query("api_key") String apiKey,
                                           @Query("query") String query,
                                           @Query("page") int page,
                                           @Query("language") String language);

    /**
     *<p>
     * Gets a list movie basic information of Now PLaying Top for the given language and page. In case of the given language isn't
     * available for some field the default language is used instead. Corresponds to HTTP requests
     * in the form:
     *
     * GET {@literal https://api.themoviedb.org/3/movie/now_playing?api_key=apiKey}
     *</p>
     *
     * @param apiKey
     *          The application's API key.
     * @param page
     *          The result page wanted.
     * @param language
     *          The language to be used in the query (e.g. pt, en).

     * @return A object that contains a List os movie basic information on NowPlaying Top.
     */
    @GET("/3/movie/now_playing")
    Call<MovieListShortInfoDTO> getNowPlaying(@Query("api_key") String apiKey,
                                          @Query("page") int page,
                                          @Query("language") String language);

    /**
     *<p>
     * Gets a list movie basic information of Up Coming Top for the given language and page. In case of the given language isn't
     * available for some field the default language is used instead. Corresponds to HTTP requests
     * in the form:
     *
     * GET {@literal https://api.themoviedb.org/3/movie/upcoming?api_key=apiKey}
     *</p>
     *
     * @param apiKey
     *          The application's API key.
     * @param page
     *          The result page wanted.
     * @param language
     *          The language to be used in the query (e.g. pt, en).

     * @return A object that contains a List os movie basic information on Up Coming Top.
     */
    @GET("/3/movie/upcoming")
    Call<MovieListShortInfoDTO> getUpComing(@Query("api_key") String apiKey,
                                        @Query("page") int page,
                                        @Query("language") String language);

    /**
     *<p>
     * Gets a list movie basic information of Popular Top for the given language and page. In case of the given language isn't
     * available for some field the default language is used instead. Corresponds to HTTP requests
     * in the form:
     *
     * GET {@literal https://api.themoviedb.org/3/movie/popular?api_key=apiKey}
     *</p>
     *
     * @param apiKey
     *          The application's API key.
     * @param page
     *          The result page wanted.
     * @param language
     *          The language to be used in the query (e.g. pt, en).

     * @return A object that contains a List os movie basic information on Popular Top.
     */
    @GET("/3/movie/popular")
    Call<MovieListShortInfoDTO> getPopular(@Query("api_key") String apiKey,
                                       @Query("page") int page,
                                       @Query("language") String language);

}
