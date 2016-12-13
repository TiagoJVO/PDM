package com.myapplication;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.myapplication.Controller.MainActivity;
/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityUnitTestCase<MainActivity>{
    private Intent mLaunchIntent;
    private boolean haSearch;
    //private MovieShortListDTO movieInfo;

    public ApplicationTest() {
        super(MainActivity.class);
    }
/*
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),MainActivity.class);
    }

    @MediumTest
    public void test_if_internet_available() {
        startActivity(mLaunchIntent, null, null);

        assertTrue(isNetworkAvailable(getActivity()));
    }

    @MediumTest
    public void test_search_movie_by_name(){

        SearchMonitor.searchForName("star", this, 1, Locale.getDefault().getLanguage());

        checkResult();
    }

    @MediumTest
    public void test_populares_movies() throws InterruptedException {

        SearchMonitor.getPopular(this, 1, Locale.getDefault().getLanguage());

        checkResult();
    }

    private boolean isNetworkAvailable(Activity mainActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(mainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkResult() {
        while(!haSearch){}
        haSearch=false;

        assertNotNull(movieInfo);

        movieInfo=null;
    }

    @Override
    public void onMovieListSearchResponse(MovieShortListDTO m) {
        this.movieInfo = m;
        haSearch = true;

    }*/
}