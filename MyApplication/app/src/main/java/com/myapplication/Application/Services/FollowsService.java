package com.myapplication.Application.Services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import com.myapplication.Controller.MovieActivity;
import com.myapplication.Application.MovieApplication;
import com.myapplication.R;
import com.myapplication.Utils.FollowedMovies;
import com.myapplication.Model.DataRepresentation.MovieInfo;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.DataRepresentation.MovieShortInfo;

public class FollowsService extends IntentService{

    private static MovieApplication ctx;

    public FollowsService() {
        super("FollowsService");
    }

    public static Intent makeIntent(MovieApplication ctx) {
        FollowsService.ctx = ctx;
        return new Intent(ctx, FollowsService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("show_notification","show_notification_acordou");
        final FollowedMovies application = ((MovieApplication) getApplication()).getFollowedMovies();

        showNotification(application.getFilteredFollowedMovies());
    }

    private void showNotification(List<MovieShortInfo> filteredFollowedMovies) {


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN  && ((MovieApplication)getApplication()).getSharedPreferences().getNotificationValue()) {
            for (MovieShortInfo movies : filteredFollowedMovies) {

                Log.v("show_notification", "show_notification_triggered: " + movies.getTitle());

                ctx.getOnlineMovieInfoProvider().getMovieInfoAsync(movies.getId(), ctx.getLanguage(), new Callback(movies));

            }

        }
    }







    private class Callback implements MovieInfoProvider.Callback {
        private final MovieShortInfo movies;
        private final NotificationManager systemService;

        public Callback(MovieShortInfo movies) {


            systemService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            this.movies = movies;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onResult(@NonNull MovieInfoProvider.CallResult result) {
            final Notification.Builder notification = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(movies.getTitle())
                    .setContentText(getResources().getString(R.string.followed_movie_released));


            Intent notifyIntent = new Intent(FollowsService.this, MovieActivity.class);
            MovieInfo movieResult = null;
            try {
                movieResult  = result.getMovieResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyIntent.putExtra("movie_to_present", movieResult);

            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pIntent =
                    PendingIntent.getActivity(
                            FollowsService.this,
                            movieResult.getId(),
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            notification.setContentIntent(pIntent);

            systemService.notify(movieResult.getId(), notification.build());
        }
    }
}
