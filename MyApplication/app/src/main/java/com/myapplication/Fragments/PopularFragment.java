package com.myapplication.Fragments;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.myapplication.R;

/**
 * <p> Fragment that represents the popular movies </p>
 * <p> On the overriden method updateWork it asks to the model search class(SearchMonitor) to the popular movies.  </p>
 * <P> Sending the respective language, page(supported on the API), and listener that is overriden in the MYfragment class</P>
 */

public class PopularFragment extends MyFragment{

    private static Resources resources;

    /**
     *<p>
     * Static initiator to return a new instance of this type of fragment properly initiated.
     * DO NOT INITIATE BY DEFAULT CONSTRUCTOR
     *</p>

     * @return The Fragment instance.
     */
    public static PopularFragment initiate(Resources resources){
        PopularFragment.resources = resources;
        PopularFragment pf = new PopularFragment();
        pf.NAME = resources.getString(R.string.popular_tab);
        return pf;
    }

    @Override
    protected void updateWork() {

        if(hasNetwork()) {
            getOnlineMovieInfoProvider().getPopularMovieInfoAsync(page, getMovieApplication().getLanguage(), this);
        }else{
            Toast.makeText(getContext(),resources.getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void setListView() {
        listView =(ListView) rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getOnlineMovieInfoProvider().getMovieInfoAsync(listItems.getResult().get(position).getId(), getMovieApplication().getLanguage(), hostActivity);
            }
        });
    }

}