package com.myapplication.Fragments;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myapplication.Controller.adapters.MovieShortAdapter;
import com.myapplication.Controller.adapters.UpComingMovieShortAdapter;
import com.myapplication.R;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.Providers.OfflineMovieProvider;

/**
 *
 * <p> Fragment that represents the up comming movies </p>
 * <p> On the overriden method updateWork it asks to the model search class(SearchMonitor) to the up comming movies.  </p>
 * <P> Sending the respective language, page(supported on the API), and listener that is overriden in the MYfragment class</P>
 */

public class UpComingFragment extends MyFragment{

    /**
     *<p>
     * Static initiator to return a new instance of this type of fragment properly initiated.
     * DO NOT INITIATE BY DEFAULT CONSTRUCTOR
     *</p>

     * @return The Fragment instance.
     */
    public static UpComingFragment initiate(Resources resources){
        UpComingFragment ucf = new UpComingFragment();
        ucf.NAME = resources.getString(R.string.up_comming_tab);
        return ucf;
    }

    @Override
    protected void updateWork() {

        OfflineMovieProvider offlineMovieInfoProvider = getOfflineMovieInfoProvider();

        if(offlineMovieInfoProvider.hasUpCommingMovieCachedResults()) {
            MovieInfoProvider.CallResult result = getOfflineMovieInfoProvider().getUpcommingMovieInfoSync(page, getMovieApplication().getLanguage());
            onResult(result);
        }else{
            if(hasNetwork()){
                getOnlineMovieInfoProvider().getUpcommingMovieInfoAsync(page, getMovieApplication().getLanguage(), this);
            }
        }
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
    protected MovieShortAdapter getMovieAdapter() {
        return new UpComingMovieShortAdapter(this.getContext(), getMovieApplication(), R.layout.list_item_follow, listItems.getResult());
    }
}