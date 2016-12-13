package com.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.myapplication.Controller.adapters.MovieShortAdapter;
import com.myapplication.Application.MovieApplication;
import com.myapplication.R;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.DataRepresentation.MovieListShortInfo;
import com.myapplication.Model.Providers.OfflineMovieProvider;
import com.myapplication.Model.Providers.OnlineMovieProvider;

/**
 *
 * <P> Class that all fragments must extend.</P>
 * <P> Is Responsible for instanciate common components shared by all fragments such as: </P>
 * <P> ->ListView </P>
 * <P> ->Bottom buttons </P>
 * <P> ->Layout </P>
 * <P> ->movie list listener response</P>
 */
public abstract class MyFragment extends Fragment  implements MovieInfoProvider.Callback{

    /** The Fragment Name to identify*/
    protected String NAME;
    /** The Minimum number of pages on requests*/
    public static final int MIN_PAGES = 1;
    /** The Maximum number of pages on requests*/
    public static final int MAX_PAGES = 1000;
    /** The Listener to notify when a movie detail is requested*/
   // private OnMovieInfoListener listener;
    /** Aux RootView to speedup accesses*/
    protected View rootView;
    /** ListView that shows to user the current information*/
    protected ListView listView;
    /** List that holds the current information*/
    protected MovieListShortInfo listItems;
    /** Current page on UI pages of search*/
    protected int page;
    /** The Listener to notify when a movie detail is requested*/
    protected MovieInfoProvider.Callback hostActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);//set this fragment retained, to hold it when the activity is destroyed
    }

    protected MovieApplication getMovieApplication(){

        return (MovieApplication) getActivity().getApplication();
    }

    protected OnlineMovieProvider getOnlineMovieInfoProvider(){

        return getMovieApplication().getOnlineMovieInfoProvider();
    }

    protected OfflineMovieProvider getOfflineMovieInfoProvider(){

        return getMovieApplication().getOfflineMovieInfoProvider();
    }

    protected boolean hasNetwork(){
        return getMovieApplication().isNetworkAvailable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState==null) {//if the fragment has state(for example when rotating device) doesn´t update view
            rootView = inflater.inflate(R.layout.list_fragment, container, false);

            setListView();

            setButtonsActions();

            updateWork();
        }

        return rootView;
    }

    /**
     *<p>
     * Sets the buttons actions to be made when clicked.
     *</p>
     */
    protected void setButtonsActions() {
        page = MIN_PAGES;

        ImageView prev = (ImageView)rootView.findViewById(R.id.left_button);
        ImageView next = (ImageView)rootView.findViewById(R.id.right_button);
        ImageView refresh = (ImageView)rootView.findViewById(R.id.refresh_button);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWork();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page > MIN_PAGES) {
                    page -= 1;
                    updateWork();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page < MAX_PAGES) {
                    page += 1;
                    updateWork();
                }
            }
        });
    }

    /**
     *<p>
     * Sets the view actions to be made when click an item.
     *getOnlineMovieInfoProvider
     * </p>
     */
    protected abstract void setListView();

    /**
     *<p>
     * Gets The fragment name.
     *</p>

     * @return The fragment name.
     */
    public String getName(){
        return NAME;
    }

    /**
     *<p>
     * do All da update work to be made when a request is made. Depends from the Fragment type
     *</p>
     */
    protected abstract void updateWork();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (MovieInfoProvider.Callback) getActivity();
    }

    @Override
    public void onResult(@NonNull MovieInfoProvider.CallResult result) {
        try {
            listItems = result.getMovieListResult();
            MovieShortAdapter adapter = getMovieAdapter();
            listView.setAdapter(adapter);

        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg != null)
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), R.string.error_on_get_API_info, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * To get the correspondent adapter of the list.
     * This is a method because upcommingfragment needs another layour/adapter for present.
     * if a fragment needs to implement other adapters override this method
     *
     * @return MovieShortAdapter
     */
    protected MovieShortAdapter getMovieAdapter() {
        return new MovieShortAdapter(this.getContext(), getMovieApplication(), R.layout.list_item, listItems.getResult());
    }
}
