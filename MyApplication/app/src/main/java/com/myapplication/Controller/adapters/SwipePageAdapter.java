package com.myapplication.Controller.adapters;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myapplication.Fragments.MyFragment;
import com.myapplication.Fragments.NowPlayingFragment;
import com.myapplication.Fragments.PopularFragment;
import com.myapplication.Fragments.SettingsFragment;
import com.myapplication.Fragments.UpComingFragment;
import com.myapplication.Fragments.SearchFragment;


/**
 *
 * This class is the controller of the pages(represented as fragments) of the swipe view.
 *
 * In the getItem is instanciated each fragment with the respective order.
 */
public class SwipePageAdapter extends FragmentPagerAdapter{


    public static final int FRAGMENT_NUMBER = 5;
    private final Resources resources;

    /**
     *
     * @param fm used to super()
     * @param resources to the fragments get the resources of the activity
     * //@param listener when, in each fragment, is clicked on a specific movie in the list it's called the method of this listener
     */
    public SwipePageAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public MyFragment getItem(int i) {

        switch (i) {
            case 0:
                return PopularFragment.initiate(resources);
            case 1:
                return NowPlayingFragment.initiate(resources);
            case 2:
                return UpComingFragment.initiate(resources);
            case 3:
                return SearchFragment.initiate(resources);
            case 4:
                return SettingsFragment.initiate(resources);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return getItem(position).getName();
    }



}
