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
import com.myapplication.Model.DataRepresentation.MovieShortInfo;

public class MovieNowPlayingPageRefreshService extends IntentService {

    private static final String EXTRA_PAGE = "MovieNowPlayingPageRefreshService.Extra.Page";
    private static final String EXTRA_SIZE = "MovieNowPlayingPageRefreshService.Extra.Size";

    public static Intent makeIntent(Context ctx,int page, int size) {
        return new Intent(ctx, MovieNowPlayingPageRefreshService.class)
                .putExtra(EXTRA_PAGE,page)
                .putExtra(EXTRA_SIZE,size);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MovieNowPlayingPageRefreshService() {
        super("MovieNowPlayingPageRefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final MovieApplication application = (MovieApplication) getApplication();
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        try {
            Log.v("INTENT_SERVICE", "MovieNowPlayingPageRefreshService start page " + intent.getIntExtra(EXTRA_PAGE,1));
            request(intent.getIntExtra(EXTRA_PAGE,1),intent.getIntExtra(EXTRA_SIZE,1), application,alarmManager);
            Log.v("INTENT_SERVICE", "MovieNowPlayingPageRefreshService done page " + intent.getIntExtra(EXTRA_PAGE,1));
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }

    private void request(int page, int size, MovieApplication application,AlarmManager alarmManager) {
        try {
            final MovieInfoProvider.CallResult info = application.getOnlineMovieInfoProvider().
                    getNowPlayingMovieInfoSync(page, application.getLanguage());

            application.getOfflineMovieInfoProvider().nowPlayingListUpdate(info);
            MovieListShortInfo result = info.getMovieListResult();

            long step = 2*1000;
            long timer = 1000;
            for (MovieShortInfo movie : result.getResult()) {
                final PendingIntent movieIntent = PendingIntent.getService(
                        application, movie.getId(),
                        MovieRefreshService.makeIntent(application, movie.getId()),
                        PendingIntent.FLAG_ONE_SHOT
                );
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timer, movieIntent);
                timer += step;
                Log.v("INTENT_SERVICE", "MovieNowPlayingPageRefreshService request movie " + movie.getTitle());
            }
            int nextPage = page+1;
            if(nextPage>size)
                return;

            timer = 120 * 1000;
            final PendingIntent pendingIntent = PendingIntent.getService(
                    application, MovieApplication.REFRESHER_N_SERVICE_CODE+nextPage,
                    MovieNowPlayingPageRefreshService.makeIntent(application,nextPage,size),
                    PendingIntent.FLAG_ONE_SHOT
            );
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timer, pendingIntent);
            Log.v("INTENT_SERVICE", "MovieNowPlayingPageRefreshService request Now page " + nextPage);
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }
}