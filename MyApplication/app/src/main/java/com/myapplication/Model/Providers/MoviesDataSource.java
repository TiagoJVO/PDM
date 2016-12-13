package com.myapplication.Model.Providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.myapplication.Model.DataRepresentation.MovieInfo;
import com.myapplication.Model.DataRepresentation.MovieListShortInfo;
import com.myapplication.Model.DataRepresentation.MovieShortInfo;
import com.myapplication.Model.Providers.SQL.MySqlLite;

/**
 * classe que é responsável por encapsular a informação de um filme/lista,
 * para depois com a ajuda do content provider inserir/apagar/selecionar info da BD
 * */
public class MoviesDataSource{

    private Context context;

    public MoviesDataSource(Context context) {
        this.context = context;
    }
    /**
     * Colocar na BD informação relativa a um movie, para tal cria-se um objecto Content Value, guardando-se aí a informação do filme,
     * para utilizar na chamada do content provider.
     * No caso do field Genre,Company e Country, como constituem arrays, decidimos construir uma string separada por "," para facilitar a construção da BD(daí os 3 ciclos).
     * Se a BD já conter o movie, não se insere novamente.
     * */
    public void putMovie(MovieInfo movie) {

        ContentValues values = new ContentValues();
        values.put(MySqlLite.Movie.ID,movie.getId());
        values.put(MySqlLite.Movie.HOMEPAGE,movie.getHomepage());
        values.put(MySqlLite.Movie.POSTER_PATH,movie.getPoster_path());
        values.put(MySqlLite.Movie.TAGLINE,movie.getTagline());
        values.put(MySqlLite.Movie.TITLE,movie.getTitle());
        values.put(MySqlLite.Movie.OVERVIEW,movie.getOverview());
        values.put(MySqlLite.Movie.RELEASE_DATE,movie.getReleaseDate());
        values.put(MySqlLite.Movie.RATING,movie.getRating());
        String companies = "";
        for (MovieInfo.Company comp :movie.getCompanies()){
            companies+=comp.getName()+",";
        }
        companies = companies.substring(0, companies.length()-1);
        String genres = "";
        for (MovieInfo.Gender gen :movie.getGenderList()){
            genres+=gen.getName()+",";
        }
        genres = genres.substring(0, genres.length()-1);
        String countries = "";
        for (MovieInfo.Country count :movie.getCountries()){
            countries+=count.getName()+",";
        }
        countries = countries.substring(0, countries.length()-1);
        values.put(MySqlLite.Movie.COMPANIES,companies);
        values.put(MySqlLite.Movie.GENRES,genres);
        values.put(MySqlLite.Movie.COUNTRIES, countries);
        if(!checkIfExists(Uri.parse(MyContentProvider.CONTENT_URI_MOVIES + "/" + movie.getId()),MySqlLite.Movie.getColumNames())) {//se não existir o filme adiciona a BD
            //não faz sentido actualizar porque estes raramente sao modificados
            context.getContentResolver().insert(MyContentProvider.CONTENT_URI_MOVIES, values);
        }
    }
    /**
     * Coloca na BD a movieShort info dos Upcomming Movies.
     * Se já existir a página indicada no upCommingMovieList passado em parametro, então remove-se todos os tuplos da tabela upcomming da BD com essa página.
     * No ciclo vai-se inserido a informação de cada movie short de upcomming list na BD.
     * */
    public void putUpCommingMovie(MovieListShortInfo upCommingMovie) {
        if(checkIfExists(Uri.parse(MyContentProvider.CONTENT_URI_UPCOMMING + "/" + upCommingMovie.getPage()),MySqlLite.UpCommingMovies.getColumNames())){//apaga a pagina x dos upcomming para actulizar com as novas
           context.getContentResolver().delete(Uri.parse(MyContentProvider.CONTENT_URI_UPCOMMING + "/" + upCommingMovie.getPage()), null, null);
        }

        for (MovieShortInfo m : upCommingMovie.getResult()) {
            ContentValues values = new ContentValues();
            values.put(MySqlLite.UpCommingMovies.PAGE,upCommingMovie.getPage());
            values.put(MySqlLite.UpCommingMovies.MOVIES,m.getId());
            values.put(MySqlLite.UpCommingMovies.IS_ADULT,m.isAdult()+"");
            values.put(MySqlLite.UpCommingMovies.OVERVIEW,m.getOverview());
            values.put(MySqlLite.UpCommingMovies.POSTER_PATH,m.getPoster_path());
            values.put(MySqlLite.UpCommingMovies.TITLE,m.getTitle());
            values.put(MySqlLite.UpCommingMovies.RATING,m.getRating());
            values.put(MySqlLite.UpCommingMovies.RELEASE_DATE, m.getReleaseDate());
            context.getContentResolver().insert(MyContentProvider.CONTENT_URI_UPCOMMING, values);
        }
    }
/**
 * Este método é identico ao putUpCommingMovie, mas relativo aos filmes em exibição.
 * */
    public void putNowPlayingMovie(MovieListShortInfo nowPlayingMovie) {
        if(checkIfExists(Uri.parse(MyContentProvider.CONTENT_URI_NOWPLAYING + "/" + nowPlayingMovie.getPage()),MySqlLite.NowPlayingMovies.getColumNames())){//apaga a pagina x dos now playing para actulizar com as novas
            context.getContentResolver().delete(Uri.parse(MyContentProvider.CONTENT_URI_NOWPLAYING + "/" + nowPlayingMovie.getPage()), null, null);
        }

        for (MovieShortInfo m : nowPlayingMovie.getResult()) {
            ContentValues values = new ContentValues();
            values.put(MySqlLite.NowPlayingMovies.PAGE, nowPlayingMovie.getPage());
            values.put(MySqlLite.NowPlayingMovies.MOVIES,m.getId());
            values.put(MySqlLite.NowPlayingMovies.IS_ADULT,m.isAdult()+"");
            values.put(MySqlLite.NowPlayingMovies.OVERVIEW,m.getOverview());
            values.put(MySqlLite.NowPlayingMovies.POSTER_PATH,m.getPoster_path());
            values.put(MySqlLite.NowPlayingMovies.TITLE,m.getTitle());
            values.put(MySqlLite.NowPlayingMovies.RATING,m.getRating());
            values.put(MySqlLite.NowPlayingMovies.RELEASE_DATE, m.getReleaseDate());
            context.getContentResolver().insert(MyContentProvider.CONTENT_URI_NOWPLAYING, values);
        }
    }

/**
 * Verifica atraves do uri passado em parametro, se esse componente existe(verificando se o cursor esta vazio).
 * */
    private boolean checkIfExists(Uri u,String[] projection) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(u,projection,null,null,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
/**
 * Seleciona todos os filmes em exibição,para uma certa pagina indicada em parametro, guardados na BD.
 * atraves de um cursor vai percorrendo os tuplos, guardando os campos da tabela no objecto MovieShortInfo, e adiciona á lista que ira ser retornada.
 * */
    public MovieListShortInfo getNowPlayingMovie(int page) {
        List<MovieShortInfo> list = new LinkedList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse(MyContentProvider.CONTENT_URI_NOWPLAYING + "/" + page), MySqlLite.NowPlayingMovies.getColumNames(), null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new MovieShortInfo(cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.TITLE)),
                                        Boolean.getBoolean(cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.IS_ADULT))),
                                        cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.OVERVIEW)),
                                        cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.RELEASE_DATE)),
                                        cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.POSTER_PATH)),
                                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.RATING))),
                                        cursor.getInt(cursor.getColumnIndex(MySqlLite.NowPlayingMovies.MOVIES))
                                        )
                    );
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list.isEmpty() ? null : new MovieListShortInfo(list,1,page);//number of pages???????
    }

    /**
     * Método igual ao getNowPlayingMovie mas para os filmes com estreia em breve.
     * */
    public MovieListShortInfo getUpCommingMovie(int page) {
        List<MovieShortInfo> list = new LinkedList<>();
        Cursor cursor = context.getContentResolver().query(Uri.parse(MyContentProvider.CONTENT_URI_UPCOMMING + "/" + page), MySqlLite.UpCommingMovies.getColumNames(), null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new MovieShortInfo(cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.TITLE)),
                            Boolean.getBoolean(cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.IS_ADULT))),
                            cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.OVERVIEW)),
                            cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.POSTER_PATH)),
                            Double.parseDouble(cursor.getString(cursor.getColumnIndex(MySqlLite.UpCommingMovies.RATING))),
                            cursor.getInt(cursor.getColumnIndex(MySqlLite.UpCommingMovies.MOVIES))
                    )
            );
            cursor.moveToNext();
        }
        cursor.close();
        return list.isEmpty() ? null : new MovieListShortInfo(list,1,page);//number of pages???????
    }
/**
 * Seleciona, atraves do content provider, o filme pertencente ao id passado em parametro.
 * atraves do cursor selectiona o tuplo e cria o objecto MovieInfo, retornando-o no final.
 * */
    public MovieInfo getMovie(int id) {

        Cursor cursor = context.getContentResolver().query(Uri.parse(MyContentProvider.CONTENT_URI_MOVIES + "/" + id), MySqlLite.Movie.getColumNames(), null, null, null);
        MovieInfo result = null;
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            result = new MovieInfo( cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.TITLE)),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.RELEASE_DATE)),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.OVERVIEW)),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.TAGLINE)),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.RATING)),
                                    arrayToGender(getListOf(cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.GENRES)))),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.HOMEPAGE)),
                                    arrayToCompany(getListOf(cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.COMPANIES)))),
                                    arrayToCountry(getListOf(cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.COUNTRIES)))),
                                    cursor.getString(cursor.getColumnIndex(MySqlLite.Movie.POSTER_PATH)),
                                    cursor.getInt(cursor.getColumnIndex(MySqlLite.Movie.ID))
                                );
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }
/**
 * método que dada uma lista de strings(contendo informação do nome do country) em parametro,
 * adiciona instancias de Country a uma lista que mais tarde sera usada para ser associada á informação de um MovieInfo
 * */
    private ArrayList<MovieInfo.Country> arrayToCountry(List<String> listOf) {

        ArrayList<MovieInfo.Country> result= new ArrayList<>();

        for (String s: listOf) {
            result.add(new MovieInfo.Country("null",s));
        }

        return result;
    }
    /**
     * método que dada uma lista de strings(contendo informação do nome do Company) em parametro,
     * adiciona instancias de Company a uma lista que mais tarde sera usada para ser associada á informação de um MovieInfo
     * */
    private ArrayList<MovieInfo.Company> arrayToCompany(List<String> listOf) {
        ArrayList<MovieInfo.Company> result= new ArrayList<>();

        for (String s: listOf) {
            result.add(new MovieInfo.Company(0,s));
        }

        return result;

    }
    /**
     * método que dada uma lista de strings(contendo informação do nome do Gender) em parametro,
     * adiciona instancias de Gender a uma lista que mais tarde sera usada para ser associada á informação de um MovieInfo
     * */
    private ArrayList<MovieInfo.Gender> arrayToGender(List<String> listOf) {
        ArrayList<MovieInfo.Gender> result= new ArrayList<>();

        for (String s: listOf) {
            result.add(new MovieInfo.Gender(0,s));
        }

        return result;
    }

    private List<String> getListOf(String string) {
        return Arrays.asList(string.split(","));
    }
/**
 * faz a verificação, perguntando ao content provider, se existe a 1ª página da lista de movies em exibição
 * */
    public boolean hasNowPlayingMovies() {

        return checkIfExists(Uri.parse(MyContentProvider.CONTENT_URI_NOWPLAYING + "/" + 1),MySqlLite.NowPlayingMovies.getColumNames());

    }
    /**
     * faz a verificação, perguntando ao content provider, se existe a 1ª página da lista de movies com estreia
     * */
    public boolean hasUpcommingMovies() {
        return checkIfExists(Uri.parse(MyContentProvider.CONTENT_URI_UPCOMMING + "/" + 1),MySqlLite.UpCommingMovies.getColumNames());

    }
}

