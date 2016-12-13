package com.myapplication.Application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.myapplication.Utils.FollowedMovies;
import com.myapplication.Application.Services.FollowsService;
import com.myapplication.Application.Services.MovieListRefreshService;
import com.myapplication.Model.Providers.OfflineMovieProvider;
import com.myapplication.Model.Providers.OnlineMovieProvider;


public class MovieApplication extends Application {
    private static final long TIMER = AlarmManager.INTERVAL_DAY;
    public static final int REFRESHER_A_SERVICE_CODE = 1;
    public static final int REFRESHER_U_SERVICE_CODE = 200;
    public static final int REFRESHER_N_SERVICE_CODE = 300;
    public static final int FOLLOW_SERVICE_CODE = 5;

    /** The current language. */
    private String language;
    /** The instance that provides the service of providing movie information. */
    private volatile OnlineMovieProvider onlineMovieInfoProvider;
    private volatile OfflineMovieProvider offlineMovieInfoProvider;
    private volatile FollowedMovies followedMovies = null;
    private SharedPreferencesManager sharedPreferencesManager;

    /**
     * Initializes the language
     */
    private void initLanguageConfiguration(Configuration config) {
        language = config.locale.getLanguage();
    }


    public SharedPreferencesManager getSharedPreferences(){ return sharedPreferencesManager;}



    /**
     * @return The current language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return The existing movie provider instance.
     */
    public OnlineMovieProvider getOnlineMovieInfoProvider() {
        return onlineMovieInfoProvider;
    }

    public OfflineMovieProvider getOfflineMovieInfoProvider() {
        return offlineMovieInfoProvider;
    }

    public FollowedMovies getFollowedMovies(){

        if(followedMovies==null){
            Log.v("show_notification","followedMovies a nulo");
        }else{
            //Log.v("show_notification","followedMovies nao nulo "+followedMovies.getFilteredFollowedMovies().isEmpty());
        }
        return followedMovies;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate() {
        super.onCreate();

        if(onlineMovieInfoProvider != null)
            return;

        initLanguageConfiguration(getResources().getConfiguration());

        //So pode ser iniciado uma vez....
        // Instantiate the concrete weather provider implementation
        sharedPreferencesManager = new SharedPreferencesManager(this.getBaseContext());
        onlineMovieInfoProvider = new OnlineMovieProvider(this);
        offlineMovieInfoProvider = new OfflineMovieProvider(this);


        if(followedMovies == null)
            followedMovies = new FollowedMovies(this);

        final AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        final PendingIntent pendingIntent = PendingIntent.getService(
                this, REFRESHER_A_SERVICE_CODE,
                MovieListRefreshService.makeIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        final PendingIntent pendingIntent_followers = PendingIntent.getService(
                this, FOLLOW_SERVICE_CODE,
                FollowsService.makeIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, sharedPreferencesManager.getTimerRefresherServiceId(), pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, TIMER, pendingIntent_followers);
    }


    /** {@inheritDoc} */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLanguageConfiguration(newConfig);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.v("show_notification","APP TERMINOU");
    }
}
