package com.myapplication.Controller.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import com.myapplication.Application.MovieApplication;
import com.myapplication.R;
import com.myapplication.Model.Providers.MovieInfoProvider;
import com.myapplication.Model.DataRepresentation.MovieShortInfo;
import com.myapplication.Model.Providers.OnlineMovieProvider;

/**
 *
 * <p>Class responsible for controlling the short information to show in each movie of the list
 * presented by the fragments(pages of the swipe view). </p>
 */
public class MovieShortAdapter extends ArrayAdapter<MovieShortInfo>  implements  MovieInfoProvider.Callback {

    Context context;
    protected MovieApplication movieApplication;
    private OnlineMovieProvider onlineMovieInfoProvider;
    int layoutResourceId;
    List<MovieShortInfo> data = null;

    public MovieShortAdapter(Context context, MovieApplication movieApplication, int layoutResourceId, List<MovieShortInfo> data){
        super(context, layoutResourceId,data);
        this.context = context;
        this.movieApplication = movieApplication;
        this.onlineMovieInfoProvider = movieApplication.getOnlineMovieInfoProvider();
        this.layoutResourceId = layoutResourceId;
        this.data = data;

    }

    /**
     * <p>
     * This method will be called for every item in the ListView to create views
     * with their properties set as we want. </p>
     * <p> The getView method is also using a temporary holder class --> MovieShortHolder. </p>
     * <p>
     * This class will be used to cache the ImageView and TextView so they can be reused for every row
     * in the ListView and this will provide us a great performance improvement as we are recycling
     * the same two views with different properties and we don’t need to find ImageView and TextView
     * for every ListView item.
     * </p>
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieShortHolder holder = null;

        if(convertView == null)//get the components in the layout and store them in the convertView
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new MovieShortHolder();

            findHolderElems(holder, convertView);

            convertView.setTag(holder);
        }
        else//if already has the view then present that view
        {
            holder = (MovieShortHolder)convertView.getTag();
        }

        MovieShortInfo movie = data.get(position);//get the data from the returned info list and associate that info to the components
        if(holder.ms != movie) {
            setHolderElems(holder,movie);
            onlineMovieInfoProvider.getPosterImageAsync(movie.getId(), movie.getPoster_path(), holder.imgIcon, this);// also get the image from the api with respective path
        }
        return convertView;
    }

    protected void setHolderElems(MovieShortHolder holder, MovieShortInfo movie) {

        holder.ms = movie;
        holder.title.setText((movie.getTitle() != null ? movie.getTitle() : "NULL"));
        holder.releaseDate.setText(context.getResources().getString(R.string.release_date) + (movie.getReleaseDate() != null ? movie.getReleaseDate() : context.getResources().getString(R.string.Null)));
        holder.rating.setRating((float) movie.getRating());
        holder.imgIcon.setImageResource(R.drawable.no_image_available);
        onlineMovieInfoProvider.getPosterImageAsync(movie.getId(),movie.getPoster_path(), holder.imgIcon, this);// also get the image from the api with respective path
    }

    protected void findHolderElems(MovieShortHolder holder, View convertView) {
        holder.imgIcon = (ImageView)convertView.findViewById(R.id.posterImage);
        holder.title = (TextView)convertView.findViewById(R.id.filmName);
        holder.releaseDate = (TextView)convertView.findViewById(R.id.releaseDate);
        holder.rating = (RatingBar)convertView.findViewById(R.id.ratingBar);
    }

    @Override
    public void onResult(@NonNull MovieInfoProvider.CallResult result) {
        try {
            Bitmap bm = result.getImage();
            ImageView iv = result.getImageView();
            if (bm!=null)
                iv.setImageBitmap(bm);
            else
                iv.setImageResource(R.drawable.no_image_available);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static class MovieShortHolder
    {
        MovieShortInfo ms;
        ImageView imgIcon;
        TextView title;
        TextView releaseDate;
        RatingBar rating;
        ImageView follow;
    }


}
