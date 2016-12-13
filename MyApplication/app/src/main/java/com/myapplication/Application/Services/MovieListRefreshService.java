package com.myapplication.Application.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.myapplication.Application.MovieApplication;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.DataRepresentation.MovieListShortInfo;

public class MovieListRefreshService extends IntentService{

    public MovieListRefreshService() {
        super("MovieListRefreshService");
    }

    public static Intent makeIntent(MovieApplication ctx) {
        return new Intent(ctx, MovieListRefreshService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final MovieApplication application = (MovieApplication) getApplication();
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        try {
            Log.v("INTENT_SERVICE", "MovieListRefreshService start");
            requestNowPlaying(application, alarmManager);
            requestUpComming(application, alarmManager);
            Log.v("INTENT_SERVICE", "MovieListRefreshService done");
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }

    private void requestUpComming(MovieApplication application,AlarmManager alarmManager) {
        try {
            final MovieInfoProvider.CallResult info = application.getOnlineMovieInfoProvider().
                    getUpcommingMovieInfoSync(1, application.getLanguage());


            MovieListShortInfo result = info.getMovieListResult();

            long timer = 1000;
            final PendingIntent pendingIntent = PendingIntent.getService(
                    application, MovieApplication.REFRESHER_U_SERVICE_CODE+result.getPage(),
                    MovieUpCommingPageRefreshService.makeIntent(application,result.getPage(),result.getNumberOfPages()),
                    PendingIntent.FLAG_ONE_SHOT
            );
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+timer, pendingIntent);
            Log.v("INTENT_SERVICE", "MovieListRefreshService request Up page " + result.getPage());
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }

    private void requestNowPlaying(MovieApplication application,AlarmManager alarmManager) {
        try {
            final MovieInfoProvider.CallResult info = application.getOnlineMovieInfoProvider().
                    getNowPlayingMovieInfoSync(1, application.getLanguage());


            MovieListShortInfo result = info.getMovieListResult();

            long timer = 1000;
            final PendingIntent pendingIntent = PendingIntent.getService(
                    application, MovieApplication.REFRESHER_N_SERVICE_CODE+result.getPage(),
                    MovieNowPlayingPageRefreshService.makeIntent(application, result.getPage(),result.getNumberOfPages()),
                    PendingIntent.FLAG_ONE_SHOT
            );

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+timer, pendingIntent);
            Log.v("INTENT_SERVICE", "MovieListRefreshService request Now page " + result.getPage());
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }
}