package com.myapplication.Model.Providers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.myapplication.Model.DataRepresentation.MovieInfo;
import com.myapplication.Model.DataRepresentation.MovieListShortInfo;

/**
 * Classe responsável por guardar informação de um filme(chamado quando o service é activo), da lista de filmes com estreia para breve, e da lista de filmes em exibição numa BD para ficar persistente.
 * Tambem obtem as varias informações dos filmes, inseridas previamente na BD, caso não exista coneção á internet.
 * Faz uso De uma Classe MoviesDataSource, que é responsável por encapsular a informação de um filme, para depois com a ajuda do content provider inserir/apagar/selecionar info da BD.
 * */
public class OfflineMovieProvider implements MovieInfoProvider,MovieInfoProvider.MovieInfoProviderSync {

    private final MoviesDataSource datasource;

    public OfflineMovieProvider(Context ctx) {
        datasource = new MoviesDataSource(ctx);
    }

    public void upComingListUpdate(@NonNull CallResult result) {
        try {
            datasource.putUpCommingMovie(result.getMovieListResult());

        } catch (Exception e) {
            Log.v("sql error",e.getMessage());
        }
    }

    public void nowPlayingListUpdate(@NonNull CallResult result) {
        try {
            datasource.putNowPlayingMovie(result.getMovieListResult());
        } catch (Exception e) {
            Log.v("sql error",e.getMessage());
        }
    }

    public void movieListUpdate(@NonNull CallResult result) {
        try {
            datasource.putMovie(result.getMovieResult());
        } catch (Exception e) {
            Log.v("sql error",e.getMessage());
        }
    }

    @Override
    public CallResult getPopularMovieInfoSync(@NonNull int page, @NonNull String language) {
        //not implemented
        return null;
    }

    @Override
    public CallResult getNowPlayingMovieInfoSync(@NonNull int page, @NonNull String language) {

        MovieListShortInfo mlist = datasource.getNowPlayingMovie(page);
        if(mlist != null)
            return new CallResult(mlist);
        else
            return new CallResult(new Exception("Page not defined yet"));

    }

    @Override
    public CallResult getUpcommingMovieInfoSync(@NonNull int page, @NonNull String language) {
        MovieListShortInfo res = datasource.getUpCommingMovie(page);
        if(res != null)
            return new CallResult(res);
        else
            return new CallResult(new Exception("Page not defined yet"));
    }

    @Override
    public CallResult getSearchMovieInfoSync(@NonNull int page, @NonNull String name, @NonNull String language) {
        //not implemented
        return null;
    }

    @Override
    public CallResult getMovieInfoSync(@NonNull int id, @NonNull String language) {

        MovieInfo res = datasource.getMovie(id);
        if(res != null)
            return new CallResult(res);
        else
            return new CallResult(new Exception("Movie not ready yet"));
    }

    @Override
    public CallResult getPosterImageSync(@NonNull int movieId, @NonNull String poster_path, @NonNull ImageView imgIcon) {
        //not implemented
        return null;
    }

    public boolean hasNowPlayingMovieCachedResults() {
        boolean result = datasource.hasNowPlayingMovies();
        return result;
    }

    public boolean hasUpCommingMovieCachedResults() {
        boolean result = datasource.hasUpcommingMovies();
        return result;
    }
}
