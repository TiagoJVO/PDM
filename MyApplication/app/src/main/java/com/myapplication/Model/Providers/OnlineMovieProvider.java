package com.myapplication.Model.Providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.net.ConnectivityManager;
import com.myapplication.Application.MovieApplication;

import java.io.IOException;

import com.myapplication.Application.SharedPreferencesManager;
import com.myapplication.Model.DataRepresentation.DataMapper;
import com.myapplication.Model.DataRepresentation.DTO.MovieInfoDTO;
import com.myapplication.Model.DataRepresentation.DTO.MovieListShortInfoDTO;
import com.myapplication.R;
import com.myapplication.Utils.ImageCacher;
import com.myapplication.Utils.Utils;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class OnlineMovieProvider implements MovieInfoProvider,MovieInfoProvider.MovieInfoProviderAsync, MovieInfoProvider.MovieInfoProviderSync {

    /** The weather service API proxy */
    private final WebAPI serviceAPI;
    /** The data mapper used to convert DTOs. */
    private final DataMapper mapper;
    /** The connectivityManager. */
    private final ConnectivityManager connectivityManager;
    /** The shared preferences manager. */
    private final SharedPreferencesManager preferencesManager;

    private final MovieApplication application;

    private ImageCacher cacher;

    /** The base URL to access to Images DB. */
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    /**
     * Initiates an instance.
     */
    public OnlineMovieProvider(Context context) {

        // Configure retrofit object
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create web api proxy
        serviceAPI = retrofit.create(WebAPI.class);

        // Gets the connectivityManager
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        application = (MovieApplication)context;

        // Gets the shared preferences manager
        preferencesManager = application.getSharedPreferences();

        // Create the data mapper
        mapper = new DataMapper();

        cacher = new ImageCacher(context);
    }

    @Override
    public void getPopularMovieInfoAsync(@NonNull int page, @NonNull String language, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Call<MovieListShortInfoDTO> call = serviceAPI.getPopular(WebAPI.API_KEY,page,language);
        call.enqueue(new retrofit.Callback<MovieListShortInfoDTO>() {
            @Override
            public void onResponse(Response<MovieListShortInfoDTO> response, Retrofit retrofit) {
                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    @Override
    public void getNowPlayingMovieInfoAsync(@NonNull int page, @NonNull String language, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Call<MovieListShortInfoDTO> call = serviceAPI.getNowPlaying(WebAPI.API_KEY, page, language);
        call.enqueue(new retrofit.Callback<MovieListShortInfoDTO>() {
            @Override
            public void onResponse(Response<MovieListShortInfoDTO> response, Retrofit retrofit) {
                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    @Override
    public void getUpcommingMovieInfoAsync(@NonNull int page, @NonNull String language, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Call<MovieListShortInfoDTO> call = serviceAPI.getUpComing(WebAPI.API_KEY, page, language);
        call.enqueue(new retrofit.Callback<MovieListShortInfoDTO>() {
            @Override
            public void onResponse(Response<MovieListShortInfoDTO> response, Retrofit retrofit) {
                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    @Override
    public void getSearchMovieInfoAsync(@NonNull int page, @NonNull String name, @NonNull String language, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Call<MovieListShortInfoDTO> call = serviceAPI.getMovieSearch(WebAPI.API_KEY, name, page, language);
        call.enqueue(new retrofit.Callback<MovieListShortInfoDTO>() {
            @Override
            public void onResponse(Response<MovieListShortInfoDTO> response, Retrofit retrofit) {
                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    @Override
    public void getMovieInfoAsync(@NonNull int id, @NonNull String language, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Call<MovieInfoDTO> call = serviceAPI.getMovie(id, WebAPI.API_KEY, language);
        call.enqueue(new retrofit.Callback<MovieInfoDTO>() {
            @Override
            public void onResponse(Response<MovieInfoDTO> response, Retrofit retrofit) {
                final CallResult result = response.isSuccess() ?
                        new CallResult(mapper.convertFrom(response.body())) :
                        new CallResult(new Exception(response.errorBody().toString()));

                completionCallback.onResult(result);
            }

            @Override
            public void onFailure(Throwable t) {
                completionCallback.onResult(new CallResult(new Exception(t)));
            }
        });
    }

    @Override
    public void getPosterImageAsync(@NonNull final int movieId, @NonNull final String poster_path, @NonNull final ImageView iv, @NonNull final Callback completionCallback) {
        if (!checkConnection(completionCallback)) return;

        Bitmap bm = cacher.getBitmap(String.valueOf(movieId));
        if(bm != null)
            completionCallback.onResult(new CallResult(iv, bm));
        else {
            new AsyncTask<String, Object, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... subPath) {
                    return Utils.getBitMapFromURL(IMAGE_BASE_URL + subPath[0]);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    cacher.put(String.valueOf(movieId),result);
                    completionCallback.onResult(new CallResult(iv, result));
                }
            }.execute(poster_path);
        }
    }

    @Override
    public CallResult getPopularMovieInfoSync(@NonNull int page, @NonNull String language) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));
        Call<MovieListShortInfoDTO> call = serviceAPI.getPopular(WebAPI.API_KEY, page, language);
        try {
            Response<MovieListShortInfoDTO> response = call.execute();
            return response.isSuccess() ?
                    new CallResult(mapper.convertFrom(response.body())) :
                    new CallResult(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new CallResult(new Exception(e));
        }
    }

    @Override
    public CallResult getNowPlayingMovieInfoSync(@NonNull int page, @NonNull String language) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));

        Call<MovieListShortInfoDTO> call = serviceAPI.getNowPlaying(WebAPI.API_KEY, page, language);
        try {
            Response<MovieListShortInfoDTO> response = call.execute();
            return response.isSuccess() ?
                    new CallResult(mapper.convertFrom(response.body())) :
                    new CallResult(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new CallResult(new Exception(e));
        }
    }

    @Override
    public CallResult getUpcommingMovieInfoSync(@NonNull int page, @NonNull String language) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));

        Call<MovieListShortInfoDTO> call = serviceAPI.getUpComing(WebAPI.API_KEY, page, language);
        try {
            Response<MovieListShortInfoDTO> response = call.execute();
            return response.isSuccess() ?
                    new CallResult(mapper.convertFrom(response.body())) :
                    new CallResult(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new CallResult(new Exception(e));
        }
    }

    @Override
    public CallResult getSearchMovieInfoSync(@NonNull int page, @NonNull String name, @NonNull String language) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));

        Call<MovieListShortInfoDTO> call = serviceAPI.getMovieSearch(WebAPI.API_KEY, name, page, language);
        try {
            Response<MovieListShortInfoDTO> response = call.execute();
            return response.isSuccess() ?
                    new CallResult(mapper.convertFrom(response.body())) :
                    new CallResult(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new CallResult(new Exception(e));
        }
    }

    @Override
    public CallResult getMovieInfoSync(@NonNull int id, @NonNull String language) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));

        Call<MovieInfoDTO> call = serviceAPI.getMovie(id, WebAPI.API_KEY, language);
        try {
            Response<MovieInfoDTO> response = call.execute();
            return response.isSuccess() ?
                    new CallResult(mapper.convertFrom(response.body())) :
                    new CallResult(new Exception(response.errorBody().toString()));
        } catch (IOException e) {
            return new CallResult(new Exception(e));
        }
    }

    @Override
    public CallResult getPosterImageSync(@NonNull int movieId, @NonNull String poster_path, @NonNull ImageView imgIcon) {
        if (!checkConnection()) return new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection))));

        Bitmap bm = cacher.getBitmap(String.valueOf(movieId));
        if(bm != null)
            return new CallResult(imgIcon, bm);
        else {
            Bitmap result = Utils.getBitMapFromURL(IMAGE_BASE_URL + poster_path);
            cacher.put(String.valueOf(movieId), result);
            return new CallResult(imgIcon, result);
        }
    }

    private boolean checkConnection(@NonNull Callback completionCallback) {
        NetworkInfo current = connectivityManager.getActiveNetworkInfo();

        if(current == null || !preferencesManager.checkIfConnectivityMatch(current.getType()) )
        {
            completionCallback.onResult(new CallResult((new Exception(application.getResources().getString(R.string.no_internet_connection)))));
            return false;
        }
        return true;
    }

    private boolean checkConnection() {
        NetworkInfo current = connectivityManager.getActiveNetworkInfo();

        return current != null && preferencesManager.checkIfConnectivityMatch(connectivityManager.getActiveNetworkInfo().getType());
    }
}
