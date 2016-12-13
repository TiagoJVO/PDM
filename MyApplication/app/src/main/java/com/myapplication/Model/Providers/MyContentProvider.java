package com.myapplication.Model.Providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import com.myapplication.Model.Providers.SQL.MySqlLite;

/**
 * Classe que através da manipulação de uris possibilita a inserção/seleção/update/delete de objectos(movies, upcoming movies, nowplaying movies, followed movies) na BD.
 * Qualquer aplicação externa pode manipular a a Movie Db com a respectiva chamada aos uris disponibilizados.
 * Para utilização por exemplo do uri para inserir movies usa-se: CONTENT_URI_MOVIES
 * Para selecionar/actualizar/apagar um especifico Movie: CONTENT_URI_MOVIES + "/{ID do filme}", utilizando para o caso da actualização um ContentValues indicando a nova informação.
 * */
public class MyContentProvider extends ContentProvider {

    // database
    private MySqlLite database;

    // used for the UriMacher
    private static final int MOVIES = 10;
    private static final int UPCOMMING_MOVIES = 20;
    private static final int NOWPLAYING_MOVIES = 30;
    private static final int MOVIES_INSERT = 40;
    private static final int UPCOMMING_MOVIES_INSERT = 50;
    private static final int NOWPLAYING_MOVIES_INSERT = 60;
    private static final int FOLLOWED_ALL = 70;
    private static final int FOLLOWED = 80;

    private static final String AUTHORITY = "com.myapplication.Model.Providers";

    /////////////uris para utilização externa//////////////////
    private static final String BASE_PATH = "MovieDB";
    public static final Uri CONTENT_URI_MOVIES = Uri.parse("content://" + AUTHORITY+ "/" + BASE_PATH+"/movie");
    public static final Uri CONTENT_URI_UPCOMMING = Uri.parse("content://" + AUTHORITY+ "/" + BASE_PATH+"/upcomming_movie");
    public static final Uri CONTENT_URI_NOWPLAYING = Uri.parse("content://" + AUTHORITY+ "/" + BASE_PATH+"/nowplaying_movie");
    public static final Uri CONTENT_URI_FOLLOWED = Uri.parse("content://" + AUTHORITY+ "/" + BASE_PATH+"/followed_movie");
    ///////////////////////////////////////////////////////////

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/movie", MOVIES_INSERT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/upcomming_movie", UPCOMMING_MOVIES_INSERT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/nowplaying_movie", NOWPLAYING_MOVIES_INSERT);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/movie/#", MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/upcomming_movie/#", UPCOMMING_MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/nowplaying_movie/#", NOWPLAYING_MOVIES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/followed_movie", FOLLOWED_ALL);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/followed_movie/#", FOLLOWED);
    }

    /**
     * Instancia a classe que cria as tabelas sql.
     * */
    @Override
    public boolean onCreate() {
        database = new MySqlLite(getContext());
        return false;
    }

    /**
     * {@inheritDoc}
    *
     * This implementation uses a dynamic programming approach.
    */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIES:
                checkColumns(projection,MOVIES);
                queryBuilder.setTables(MySqlLite.MOVIE_TABLE);
                queryBuilder.appendWhere(MySqlLite.Movie.ID + "="+ uri.getLastPathSegment());
                break;
            case UPCOMMING_MOVIES:
                checkColumns(projection,UPCOMMING_MOVIES);
                queryBuilder.setTables(MySqlLite.UPCOMMING_MOVIE_TABLE);
                queryBuilder.appendWhere(MySqlLite.UpCommingMovies.PAGE + "="+ uri.getLastPathSegment());
                break;
            case NOWPLAYING_MOVIES:
                checkColumns(projection,UPCOMMING_MOVIES);
                queryBuilder.setTables(MySqlLite.NOWPLAYING_MOVIE_TABLE);
                queryBuilder.appendWhere(MySqlLite.NowPlayingMovies.PAGE + "="+ uri.getLastPathSegment());
                break;
            case FOLLOWED:
                checkColumns(projection,FOLLOWED);
                queryBuilder.setTables(MySqlLite.FOLLOWED_MOVIE_TABLE);
                queryBuilder.appendWhere(MySqlLite.Followed.ID + "="+ uri.getLastPathSegment());
                break;
            case FOLLOWED_ALL:
                checkColumns(projection,FOLLOWED_ALL);
                queryBuilder.setTables(MySqlLite.FOLLOWED_MOVIE_TABLE);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }
    /**
     * {@inheritDoc}
     *
     * This implementation uses a dynamic programming approach.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
    /**
     * {@inheritDoc}
     *
     * This implementation uses a dynamic programming approach.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case MOVIES_INSERT:
                id = sqlDB.insert(MySqlLite.MOVIE_TABLE, null, values);
                break;
            case NOWPLAYING_MOVIES_INSERT:
                id = sqlDB.insert(MySqlLite.NOWPLAYING_MOVIE_TABLE, null, values);
                break;
            case UPCOMMING_MOVIES_INSERT:
                id = sqlDB.insert(MySqlLite.UPCOMMING_MOVIE_TABLE, null, values);
                break;
            case FOLLOWED_ALL:
                id = sqlDB.insert(MySqlLite.FOLLOWED_MOVIE_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }
    /**
     * {@inheritDoc}
     *
     * This implementation uses a dynamic programming approach.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case MOVIES:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MySqlLite.MOVIE_TABLE,MySqlLite.Movie.ID+ "=" + id,null);
                } else {
                    rowsDeleted = sqlDB.delete(MySqlLite.MOVIE_TABLE,MySqlLite.Movie.ID+ "=" + id+" and "+selection,selectionArgs);
                }
                break;
            case UPCOMMING_MOVIES:
                String id1 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MySqlLite.UPCOMMING_MOVIE_TABLE,MySqlLite.UpCommingMovies.PAGE+ "=" + id1,null);
                } else {
                    rowsDeleted = sqlDB.delete(MySqlLite.UPCOMMING_MOVIE_TABLE,MySqlLite.UpCommingMovies.PAGE+ "=" + id1+" and "+selection,selectionArgs);
                }
                break;
            case NOWPLAYING_MOVIES:
                String id2 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MySqlLite.NOWPLAYING_MOVIE_TABLE,MySqlLite.NowPlayingMovies.PAGE+ "=" + id2,null);
                } else {
                    rowsDeleted = sqlDB.delete(MySqlLite.NOWPLAYING_MOVIE_TABLE,MySqlLite.NowPlayingMovies.PAGE+ "=" + id2+" and "+selection,selectionArgs);
                }
                break;
            case FOLLOWED:
                String id3 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MySqlLite.FOLLOWED_MOVIE_TABLE,MySqlLite.Followed.ID+ "=" + id3,null);
                } else {
                    rowsDeleted = sqlDB.delete(MySqlLite.FOLLOWED_MOVIE_TABLE,MySqlLite.Followed.ID+ "=" + id3+" and "+selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }
    /**
     * {@inheritDoc}
     *
     * This implementation uses a dynamic programming approach.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case MOVIES:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MySqlLite.MOVIE_TABLE, values, MySqlLite.Movie.ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(MySqlLite.MOVIE_TABLE, values, MySqlLite.Movie.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case UPCOMMING_MOVIES:
                String id1 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MySqlLite.UPCOMMING_MOVIE_TABLE, values, MySqlLite.UpCommingMovies.PAGE + "=" + id1, null);
                } else {
                    rowsUpdated = sqlDB.update(MySqlLite.UPCOMMING_MOVIE_TABLE, values, MySqlLite.UpCommingMovies.PAGE + "=" + id1 + " and " + selection, selectionArgs);
                }
                break;
            case NOWPLAYING_MOVIES:
                String id2= uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MySqlLite.NOWPLAYING_MOVIE_TABLE, values, MySqlLite.NowPlayingMovies.PAGE + "=" + id2, null);
                } else {
                    rowsUpdated = sqlDB.update(MySqlLite.NOWPLAYING_MOVIE_TABLE, values, MySqlLite.NowPlayingMovies.PAGE + "=" + id2 + " and " + selection, selectionArgs);
                }
                break;
            case FOLLOWED:
                String id3 = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MySqlLite.FOLLOWED_MOVIE_TABLE, values, MySqlLite.Followed.ID + "=" + id3, null);
                } else {
                    rowsUpdated = sqlDB.update(MySqlLite.FOLLOWED_MOVIE_TABLE,values, MySqlLite.Followed.ID + "=" + id3 + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    /**
     * Metodo que verifica se para uma dada tabela em parametro(movies) existe colunas com o nome associado ao array de string
     * passado em parametro (projection)
     */
    private void checkColumns(String[] projection, int movies) {
        switch (movies) {
            case MOVIES:
                String[] available = MySqlLite.Movie.getColumNames();
                check(available,projection);
                break;
            case UPCOMMING_MOVIES:
                String[] available1 = MySqlLite.UpCommingMovies.getColumNames();
                check(available1,projection);
                break;
            case NOWPLAYING_MOVIES:
                String[] available2 = MySqlLite.NowPlayingMovies.getColumNames();
                check(available2,projection);
                break;
            case FOLLOWED:
                String[] available3 = MySqlLite.Followed.getColumNames();
                check(available3,projection);
                break;
        }
    }

    private void check(String[] available, String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
