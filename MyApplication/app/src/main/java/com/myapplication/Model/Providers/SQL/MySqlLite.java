package com.myapplication.Model.Providers.SQL;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Classe que tem como principal objectivo criar as tabelas correspondentes a uma base de dados.
 * tem tambem representado, em inner classes, o nome das colunas para as diferentes tabelas(cada tabela corresponde a uma inner classe)
 * */
public class MySqlLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movies";
    private static final int DATABASE_VERSION = 1;
    public static final String MOVIE_TABLE = "movie";
    public static final String UPCOMMING_MOVIE_TABLE = "UpComming";
    public static final String NOWPLAYING_MOVIE_TABLE = "NowPlaying";
    public static final String FOLLOWED_MOVIE_TABLE = "Followed";

    //a tabela Movie contem como chave primaria o id do filme e o resto das colunas é a informação correspondente ao filme proveniente da movie API.
    private static final String MOVIE_CREATE ="create table "+MOVIE_TABLE+
            "(" + Movie.ID+" integer primary key, "+Movie.RELEASE_DATE+" text, "+Movie.OVERVIEW+" text, "+Movie.TAGLINE+" text, "+
             Movie.RATING+" text, "+Movie.GENRES+" text, "+Movie.HOMEPAGE+" text, "+Movie.COMPANIES+" text, "+Movie.COUNTRIES+" text, "+Movie.POSTER_PATH+" text, " +Movie.TITLE+" text)";

    //a tabela Upcomming contem a chave(página que o upcomming filme pertence, chave do filme(que é foreign key para a tabela Movie)) e o resto de informações correspondente a um shorFilme
    private static final String UPCOMMING_CREATE = "create table "+UPCOMMING_MOVIE_TABLE+"("+UpCommingMovies.PAGE+" integer , "+UpCommingMovies.MOVIES+" integer,"+
            UpCommingMovies.IS_ADULT+" text,"+UpCommingMovies.TITLE+" text,"+UpCommingMovies.OVERVIEW+" text,"+UpCommingMovies.POSTER_PATH+" text,"+UpCommingMovies.RELEASE_DATE+" text,"+UpCommingMovies.RATING+" text,"+
            "FOREIGN KEY("+UpCommingMovies.MOVIES+") REFERENCES "+MOVIE_TABLE+"("+ Movie.ID+"), primary key("+UpCommingMovies.PAGE+","+UpCommingMovies.MOVIES+"))";

    //a tabela Upcomming contem a chave(página que o nowplaying filme pertence, chave do filme(que é foreign key para a tabela Movie)) e o resto de informações correspondente a um shorFilme
    private static final String NOWPLAYING_CREATE ="create table "+NOWPLAYING_MOVIE_TABLE+"("+NowPlayingMovies.PAGE+" integer , "+NowPlayingMovies.MOVIES+" integer,"+
            NowPlayingMovies.IS_ADULT+" text,"+NowPlayingMovies.TITLE+" text,"+NowPlayingMovies.OVERVIEW+" text,"+NowPlayingMovies.POSTER_PATH+" text,"+NowPlayingMovies.RELEASE_DATE+" text,"+NowPlayingMovies.RATING+" text,"+
            "FOREIGN KEY("+NowPlayingMovies.MOVIES+") REFERENCES "+MOVIE_TABLE+"("+ Movie.ID+"), primary key("+NowPlayingMovies.PAGE+","+NowPlayingMovies.MOVIES+"))";

   //contem simplesmente o id do filme que o utilizador esta a seguir
    private static final String FOLLOWED_CREATE ="create table "+FOLLOWED_MOVIE_TABLE+"("+Followed.ID+" integer primary key)";

    public MySqlLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(MOVIE_CREATE);
        database.execSQL(UPCOMMING_CREATE);
        database.execSQL(NOWPLAYING_CREATE);
        database.execSQL(FOLLOWED_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MOVIE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+UPCOMMING_MOVIE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+NOWPLAYING_MOVIE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+FOLLOWED_CREATE);
        onCreate(db);
    }


    public static class Movie {
        public static final String ID = "id";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String TAGLINE = "tagline";
        public static final String RATING = "rating";
        public static final String GENRES = "genres";
        public static final String HOMEPAGE = "homepage";
        public static final String COMPANIES = "companies";
        public static final String COUNTRIES = "countries";
        public static final String POSTER_PATH = "poster_path";

        public static String[] getColumNames() {
            return new String[]{ID,RELEASE_DATE,TITLE,OVERVIEW,TAGLINE,RATING,GENRES,HOMEPAGE,COMPANIES,COUNTRIES,POSTER_PATH};
        }
    }
    public static class UpCommingMovies {
        public static final String PAGE = "page";
        public static final String MOVIES = "movies";
        public static final String TITLE = "title";
        public static final String IS_ADULT = "isAdult";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String OVERVIEW = "overview";
        public static final String POSTER_PATH = "poster_path";
        public static final String RATING = "rating";

        public static String[] getColumNames() {
            return new String[]{PAGE,MOVIES,TITLE,IS_ADULT,RELEASE_DATE,OVERVIEW,POSTER_PATH,RATING};
        }
    }

    public static class NowPlayingMovies{
        public static final String PAGE = "page";
        public static final String MOVIES = "movies";
        public static final String TITLE = "title";
        public static final String IS_ADULT = "isAdult";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String OVERVIEW = "overview";
        public static final String POSTER_PATH = "poster_path";
        public static final String RATING = "rating";
        public static String[] getColumNames() {
            return new String[]{PAGE,MOVIES,TITLE,IS_ADULT,RELEASE_DATE,OVERVIEW,POSTER_PATH,RATING};
        }
    }


    public static class Followed {
        public static String ID = "id";

        public static String[] getColumNames() {
            return new String[]{ID};
        }
    }
}
