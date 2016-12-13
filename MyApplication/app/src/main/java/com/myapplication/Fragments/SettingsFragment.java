package com.myapplication.Fragments;

import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.myapplication.R;
import com.myapplication.Application.SharedPreferencesManager;


public class SettingsFragment extends MyFragment implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private static final int DAILY = 1000 * 60 * 60 * 24;
    private static final int WEEKLY = 1000 * 60 * 60 * 24 * 7;
    private static final int WIFI = ConnectivityManager.TYPE_WIFI;
    private static final int MOBILE = ConnectivityManager.TYPE_MOBILE;
    protected View rootView;
    private SharedPreferencesManager sharedPreferencesManager;
    private RadioGroup rGroupConnection;
    private RadioGroup rGroupTimer;
    Switch notification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = getMovieApplication().getSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (savedInstanceState==null) {//if the fragment has state(for example when rotating device) doesn´t update view

            rootView = inflater.inflate(R.layout.settings_fragment, container, false);

            initElements();

            setElementsValues();

        }

        return rootView;
    }

    @Override
    protected void setListView() {

    }

    public static SettingsFragment initiate(Resources resources){
        SettingsFragment pf = new SettingsFragment();
        pf.NAME = resources.getString(R.string.settings_tab);
        return pf;
    }

    private void setElementsValues() {

        notification.setChecked(sharedPreferencesManager.getNotificationValue());

        rGroupConnection.check(sharedPreferencesManager.getConnectionPreferenceId());

        rGroupTimer.check(sharedPreferencesManager.getTimerRefresherServiceId());
    }


    private void initElements(){

        notification = ((Switch) rootView.findViewById(R.id.notifications));
        notification.setOnCheckedChangeListener(this);

        rGroupConnection = ((RadioGroup) rootView.findViewById(R.id.connectionType));
        rGroupConnection.setOnCheckedChangeListener(this);

        rGroupTimer = (RadioGroup) rootView.findViewById(R.id.timerOfRequests);
        rGroupTimer.setOnCheckedChangeListener(this);
    }
    @Override
    protected void updateWork() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sharedPreferencesManager.setNotification(isChecked);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(group == rGroupConnection)
            sharedPreferencesManager.setConnectionType(checkedId == R.id.wifi ? WIFI : MOBILE);
        else
            sharedPreferencesManager.setTimerMovieRefresherService(checkedId == R.id.daily ? DAILY : WEEKLY);
    }
}
