package com.myapplication.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.myapplication.R;


public class SharedPreferencesManager {

    private static final int DAILY = 1000 * 60 * 60 * 24;
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    private final SharedPreferences userDefinitions;
    private  static final String SP_NAME = "userDefinitions";
    private static final String TIMER_MOVIE_REFRESH_SERVICE = "timer_refresh_service";
    private static final String CONNECTION_PREFERENCE = "connection_preference";
    private static final String NOTIFICATIONS = "notifications";
    private SharedPreferences.Editor edit;

    public SharedPreferencesManager(Context context) {
        userDefinitions = context.getSharedPreferences(SP_NAME, 0);
        edit = userDefinitions.edit();
    }

    /**
     * @return if notifications is enable.
     */
    public boolean getNotificationValue(){
        return userDefinitions.getBoolean(NOTIFICATIONS,false);
    }

    /**
     * @return The connection preferences.
     */
    public int getConnectionPreference(){
        return userDefinitions.getInt(CONNECTION_PREFERENCE, TYPE_WIFI);
    }
    /**
     * @return The interval time to refresh movies.
     */
    public int getTimerRefresherService(){
        return userDefinitions.getInt(TIMER_MOVIE_REFRESH_SERVICE, DAILY);
    }

    /**
     * @return The connection preferences id.
     */
    public int getConnectionPreferenceId(){
        int aux = userDefinitions.getInt(CONNECTION_PREFERENCE, TYPE_WIFI);

        return ConnectivityManager.TYPE_WIFI == aux ? R.id.wifi : R.id.mobileAndWifi;
    }
    /**
     * @return The interval time to refresh movies id.
     */
    public int getTimerRefresherServiceId(){
        int aux = userDefinitions.getInt(TIMER_MOVIE_REFRESH_SERVICE, DAILY);
        return aux == DAILY ? R.id.daily : R.id.weekly;
    }

    public void setNotification(boolean notification) {
        edit.putBoolean(NOTIFICATIONS, notification);
        edit.commit();
    }

    public void setConnectionType(int connectionType) {
        edit.putInt(CONNECTION_PREFERENCE, connectionType);
        edit.commit();
    }

    public void setTimerMovieRefresherService(int checkedId) {
        edit.putInt(TIMER_MOVIE_REFRESH_SERVICE, checkedId);
        edit.commit();
    }

    public boolean checkIfConnectivityMatch(int connectivity) {
        int con = getConnectionPreference();
        if(con == TYPE_WIFI && connectivity==con ||
                con == TYPE_MOBILE && (connectivity==con || connectivity == TYPE_WIFI))
            return true;
        return false;
    }
}
