package com.myapplication.Application.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.myapplication.Application.MovieApplication;
import com.myapplication.Model.Providers.MovieInfoProvider;



public class MovieRefreshService extends IntentService {

    private static final String EXTRA_ID = "MovieRefreshService.Extra.Id";

    public static Intent makeIntent(Context ctx,int id) {
        return new Intent(ctx, MovieRefreshService.class)
                .putExtra(EXTRA_ID,id);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MovieRefreshService() {
        super("MovieRefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final MovieApplication application = (MovieApplication) getApplication();
        try {
            Log.v("INTENT_SERVICE", "MovieRefreshService start id " + intent.getIntExtra(EXTRA_ID,1));
            request(intent.getIntExtra(EXTRA_ID,1), application);
            Log.v("INTENT_SERVICE", "MovieRefreshService done id" + intent.getIntExtra(EXTRA_ID,1));
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }

    private void request(int id, MovieApplication application) {
        try {
            final MovieInfoProvider.CallResult info = application.getOnlineMovieInfoProvider().
                    getMovieInfoSync(id, application.getLanguage());

            application.getOfflineMovieInfoProvider().movieListUpdate(info);

            Log.v("INTENT_SERVICE", "MovieRefreshService title " + info.getMovieResult().getTitle());
        } catch (Exception e) {
            Log.v("INTENT_SERVICE", e.toString());
        }
    }
}