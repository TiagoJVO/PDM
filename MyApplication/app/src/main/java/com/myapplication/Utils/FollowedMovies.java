package com.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.LinkedList;
import java.util.List;

import com.myapplication.Model.DataRepresentation.MovieShortInfo;
import com.myapplication.Model.Providers.MyContentProvider;
import com.myapplication.Model.Providers.SQL.MySqlLite;


public class FollowedMovies{

    private Context context;
/**
 * Método que insere o id do movieShort na BD para ficar persistente.
 * Para tal é usado o custom Content Provider.
 * 1º verifica se contem lá um elemento.
 * Se sim então significa que este método é para deixar de seguir o filme e apaga da BD.
 * Se não insere o ID respetivo
 * */
    public void follow(MovieShortInfo ms) {
        if(!hasFollow(ms.getId())) {
            ContentValues values = new ContentValues();
            values.put(MySqlLite.Followed.ID, ms.getId());
            context.getContentResolver().insert(MyContentProvider.CONTENT_URI_FOLLOWED, values);
        }else{
            context.getContentResolver().delete(Uri.parse(MyContentProvider.CONTENT_URI_FOLLOWED + "/" + ms.getId()), null, null);
        }
    }

    public FollowedMovies(Context context) {
        this.context = context;
    }
/**
 * Faz um pedido á BD para o id do movieShort atraves do content provider e verifica se existe esse tuplo.
 * */
    public boolean hasFollow(int id) {
        Cursor cursor = context.getContentResolver().query(Uri.parse(MyContentProvider.CONTENT_URI_FOLLOWED + "/" + id), MySqlLite.Followed.getColumNames(), null, null, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * metodo que vai selecionar todos os followed movies da BD e de seguida a informação dos movies para enviar uma lista com estes
     * para apresentar na notificação
     * */
    public List<MovieShortInfo> getFilteredFollowedMovies() {

        Cursor cursor = context.getContentResolver().query(MyContentProvider.CONTENT_URI_FOLLOWED , MySqlLite.Followed.getColumNames(), null, null, null);

        cursor.moveToFirst();
        List<MovieShortInfo> result = new LinkedList<>();

        while (!cursor.isAfterLast()) {
            int movieId = cursor.getInt(0);
            Cursor cursor2 = context.getContentResolver().query(Uri.parse(MyContentProvider.CONTENT_URI_MOVIES + "/" + movieId), MySqlLite.Movie.getColumNames(), null, null, null);
            cursor2.moveToFirst();
            result.add(new MovieShortInfo(cursor2.getString(cursor2.getColumnIndex(MySqlLite.Movie.TITLE)),
                            true,
                            cursor2.getString(cursor2.getColumnIndex(MySqlLite.Movie.OVERVIEW)),
                            cursor2.getString(cursor2.getColumnIndex(MySqlLite.Movie.RELEASE_DATE)),
                            cursor2.getString(cursor2.getColumnIndex(MySqlLite.Movie.POSTER_PATH)),
                            Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(MySqlLite.Movie.RATING))),
                            cursor2.getInt(cursor2.getColumnIndex(MySqlLite.Movie.ID))
                    ));
            cursor2.close();
        }

        cursor.close();
        return result;
    }
}
