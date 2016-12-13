package com.myapplication.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.myapplication.Controller.adapters.SwipePageAdapter;
import com.myapplication.R;
import com.myapplication.View.SlidingTabLayout;
import com.myapplication.Model.Providers.MovieInfoProvider;

public class MainActivity extends FragmentActivity implements MovieInfoProvider.Callback {

    public static final int FRAGMENTS_NUMBER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    /**
     * Initialize the components(setting the adapters) of this activity such as:
     * <p> The top navigation bar: slidTabLayout </p>
     * <p> The swipe pages: viewPager </p>
     */
    private void initializeComponents() {

        SwipePageAdapter swipeAdapter = new SwipePageAdapter(getSupportFragmentManager(),getResources());

        SlidingTabLayout slidTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.swipe);

        mViewPager.setOffscreenPageLimit(FRAGMENTS_NUMBER);

        mViewPager.setAdapter(swipeAdapter);
        slidTabLayout.setViewPager(mViewPager);
        slidTabLayout.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onResult(@NonNull MovieInfoProvider.CallResult result) {
        try {
            startActivity(MovieActivity.makeIntent(getBaseContext(),result.getMovieResult()));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
