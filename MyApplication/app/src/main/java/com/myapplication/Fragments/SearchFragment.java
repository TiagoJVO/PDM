package com.myapplication.Fragments;
/**
 * <p> Fragment that represents the search by name movies </p>
 * <P> This class contains his own layout(different than MyFragment Layout) because of the search Toolbar</P>
 * <p> On the overriden method updateWork it asks to the model search class(SearchMonitor) to the search by name movies.  </p>
 * <P> Sending the respective language, page(supported on the API), and listener that is overriden in the MYfragment class</P>
 */

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.myapplication.R;

public class SearchFragment extends MyFragment implements SearchView.OnQueryTextListener{

    private static Resources resources;
    private String searchQuery = null;
    /**
     *<p>
     * Static initiator to return a new instance of this type of fragment properly initiated.
     * DO NOT INITIATE BY DEFAULT CONSTRUCTOR
     *</p>

     * @return The Fragment instance.
     */
    public static SearchFragment initiate(Resources resources){
        SearchFragment.resources = resources;
        SearchFragment sf = new SearchFragment();
        sf.NAME = resources.getString(R.string.search_tab);
        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState==null){
            rootView = inflater.inflate(R.layout.search_fragment, container, false);

            ((SearchView) rootView.findViewById(R.id.searchView)).setOnQueryTextListener(this);

            setListView();

            setButtonsActions();
        }

        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchQuery = s;

        updateWork();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    protected void updateWork() {

        if(hasNetwork()) {
            getOnlineMovieInfoProvider().getSearchMovieInfoAsync(page,searchQuery, getMovieApplication().getLanguage(), this);
        }else{
            Toast.makeText(getContext(), resources.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

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