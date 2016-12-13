package com.myapplication.Controller.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import com.myapplication.Application.MovieApplication;
import com.myapplication.R;
import com.myapplication.Model.DataRepresentation.MovieShortInfo;

/**
 * This class is only used by UpCommingFragment because it needs to present a new feature(Following icon)
 * on the each movie on the list.
 * To not change the custom MovieShortAdapter this class extends the basic view but overrride the methods to get that element.
 */
public class UpComingMovieShortAdapter extends MovieShortAdapter {

    public UpComingMovieShortAdapter(Context context, MovieApplication movieApplication, int layoutResourceId, List<MovieShortInfo> data) {
        super(context, movieApplication, layoutResourceId, data);
    }

    @Override
    protected void setHolderElems(MovieShortHolder holder, MovieShortInfo movie) {
        super.setHolderElems(holder, movie);
        int pin;
        if(movieApplication.getFollowedMovies().hasFollow(holder.ms.getId())){
            pin = R.drawable.pinoff;
            holder.follow.setImageResource(pin);
        }else {
            pin = R.drawable.pin;
            holder.follow.setImageResource(pin);
        }
        holder.follow.setOnClickListener(new FollowClickListenner(holder,pin));
    }

    @Override
    protected void findHolderElems(MovieShortHolder holder, View convertView) {
        super.findHolderElems(holder, convertView);
        holder.follow = (ImageView) convertView.findViewById(R.id.follow);
    }
    
    public class FollowClickListenner implements View.OnClickListener {

        private MovieShortHolder holder;
        private int pin;

        public FollowClickListenner(MovieShortHolder holder, int pin) {

            this.holder = holder;
            this.pin = pin;
        }

        @Override
        public void onClick(View view) {

            movieApplication.getFollowedMovies().follow(holder.ms);

            switch (pin){
                case R.drawable.pin:
                    ((ImageView)view).setImageResource(R.drawable.pinoff);
                    pin=R.drawable.pinoff;
                    break;
                case R.drawable.pinoff:
                    ((ImageView)view).setImageResource(R.drawable.pin);
                    pin=R.drawable.pin;
                    break;
            }
        }
    }
}
