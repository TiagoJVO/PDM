package com.myapplication.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;


import com.myapplication.Application.MovieApplication;
import com.myapplication.R;
import com.myapplication.Model.DataRepresentation.MovieInfo;
import com.myapplication.Model.Providers.MovieInfoProvider;

public class MovieActivity extends Activity implements  MovieInfoProvider.Callback {

    private static final String MOVIE_TO_PRESENT = "movie_to_present";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_film);

        Bundle data = getIntent().getExtras();
        MovieInfo movie =  data.getParcelable(MOVIE_TO_PRESENT);

        initializeComponents(movie);

    }

    private void initializeComponents(MovieInfo movie) {

        ((TextView)(findViewById(R.id.title_film_presentation))).setText(movie.getTitle());
        ((TextView)(findViewById(R.id.releaseDate_film_presentation))).setText(movie.getReleaseDate());
        ((TextView)(findViewById(R.id.rating_film_presentation))).setText(movie.getRating());
        ((MovieApplication)getApplication()).getOnlineMovieInfoProvider().getPosterImageAsync(movie.getId(), movie.getPoster_path(), (ImageView) findViewById(R.id.poster), this);// also get the image from the api with respective path

        ((TextView)(findViewById(R.id.gender_film_presentation))).setText(movie.getGenres());

        ((TextView)(findViewById(R.id.overview_film_presentation))).setText(movie.getOverview());

    }


    public static Intent makeIntent(Context context, MovieInfo movie) {

        return new Intent(context, MovieActivity.class).putExtra(MOVIE_TO_PRESENT,movie);

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
}
