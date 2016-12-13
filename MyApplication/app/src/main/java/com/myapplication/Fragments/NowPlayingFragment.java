package com.myapplication.Fragments;

/**
 *
 * <p> Fragment that represents the now playing movies </p>
 * <p> On the overriden method updateWork it asks to the model search class(SearchMonitor) to the now playing movies.  </p>
 * <P> Sending the respective language, page(supported on the API), and listener that is overriden in the MYfragment class</P>
 */

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myapplication.R;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.Providers.OfflineMovieProvider;

public class NowPlayingFragment extends MyFragment {

    /**
     *<p>
     * Static initiator to return a new instance of this type of fragment properly initiated.
     * DO NOT INITIATE BY DEFAULT CONSTRUCTOR
     *</p>

     * @return The Fragment instance.
     */
    public static NowPlayingFragment initiate(Resources resources){
        NowPlayingFragment npf = new NowPlayingFragment();
        npf.NAME = resources.getString(R.string.now_playing_tab);
        return npf;
    }

    @Override
    protected void setListView() {
        listView =(ListView) rootView.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hostActivity.onResult(getOfflineMovieInfoProvider().getMovieInfoSync(listItems.getResult().get(position).getId(), getMovieApplication().getLanguage()));
            }
        });
    }

    @Override
    protected void updateWork() {

        OfflineMovieProvider offlineMovieInfoProvider = getOfflineMovieInfoProvider();

        if(offlineMovieInfoProvider.hasNowPlayingMovieCachedResults()) {
            MovieInfoProvider.CallResult result = getOfflineMovieInfoProvider().getNowPlayingMovieInfoSync(page, getMovieApplication().getLanguage());
            onResult(result);
        }else{
            if(hasNetwork()){
                getOnlineMovieInfoProvider().getNowPlayingMovieInfoAsync(page, getMovieApplication().getLanguage(),this);
            }
        }
    }
}