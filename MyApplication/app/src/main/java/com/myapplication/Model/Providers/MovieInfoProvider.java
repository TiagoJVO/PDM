package com.myapplication.Model.Providers;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.myapplication.Model.DataRepresentation.MovieInfo;
import com.myapplication.Model.DataRepresentation.MovieListShortInfo;


public interface MovieInfoProvider {


    /**
     * Class whose instances are used to host the asynchronous operation result. Instances are
     * immutable and therefore they can be safely shared by multiple threads.
     */
    final class CallResult {

        private ImageView iv;
        private Bitmap image;
        private MovieListShortInfo result;
        private Exception error;
        private MovieInfo movieInfo;

        /**
         * Prevent instantiation from outside code.
         * @param result The operation's result, if it executed successfully.
         * @param error The operation's error, if one occurred.
         */
        private CallResult(MovieListShortInfo result, Exception error) {
            this.result = result;
            this.error = error;
        }

        /**
         * Initiates an instance with the given result, thereby signalling successful completion.
         * @param result The operation's result.
         */
        public CallResult(@NonNull MovieListShortInfo result) {
            this(result, null);
        }

        /**
         * Initiates an instance with the given exception, thereby signalling a flawed completion.
         * @param error The operation's error.
         */
        public CallResult(@NonNull Exception error) {
            this(null, error);
        }

        public CallResult(@NonNull MovieInfo movieInfo) {
            this.movieInfo = movieInfo;
        }

        public CallResult(@NonNull ImageView iv, @NonNull Bitmap image) {
            this.iv = iv;
            this.image = image;
        }

        /**
         * Gets the operation result.
         * @return The MovieListShortInfo information.
         * @throws Exception The error that occurred while trying to get the weather information,
         * if one actually existed.
         */
        @NonNull
        public MovieListShortInfo getMovieListResult() throws Exception {
            if(error != null)
                throw error;
            return result;
        }

        @NonNull
        public MovieInfo getMovieResult() throws Exception {
            if(error != null)
                throw error;
            return movieInfo;
        }

        @NonNull
          public Bitmap getImage() throws Exception {
            return image;
        }

        @NonNull
        public ImageView getImageView() throws Exception {
            return iv;
        }
    }

    /**
     * The contract to be supported to receive completion notifications of asynchronous operations.
     */
    interface Callback {
        /**
         * Called when the corresponding asynchronous operation is completed.
         * @param result The asynchronous call result. The actual result must be obtained by calling
         *               the {@link MovieInfoProvider.CallResult} instance's.
         *               {@link CallResult} method, which produces the result or, if an
         *               error occurs, throws the exception bearing the error information.
         */
        void onResult(@NonNull CallResult result);
    }


    interface MovieInfoProviderAsync{
        /**
         * Asynchronous operation to get the current weather info for the given city. Completion is
         * signaled by calling the received callback instance.All arguments
         * must be non {@value null} values.
         *
         * @param language The language to be used on the result, encoded in ISO3.
         * @param completionCallback The callback to be executed once the operation is completed,
         *                           either successfully or in failure.
         */
        void getPopularMovieInfoAsync(@NonNull int page, @NonNull String language,
                                      @NonNull Callback completionCallback);

        void getNowPlayingMovieInfoAsync(@NonNull int page, @NonNull String language,
                                         @NonNull Callback completionCallback);

        void getUpcommingMovieInfoAsync(@NonNull int page, @NonNull String language,
                                        @NonNull Callback completionCallback);

        void getSearchMovieInfoAsync(@NonNull int page,@NonNull String name, @NonNull String language,
                                     @NonNull Callback completionCallback);

        void getMovieInfoAsync(@NonNull int id, @NonNull String language,@NonNull Callback completionCallback);


        void getPosterImageAsync(@NonNull int movieId, @NonNull String poster_path, @NonNull ImageView imgIcon,@NonNull Callback completionCallback);

    }

    interface MovieInfoProviderSync{
        CallResult getPopularMovieInfoSync(@NonNull int page, @NonNull String language);

        CallResult getNowPlayingMovieInfoSync(@NonNull int page, @NonNull String language);

        CallResult getUpcommingMovieInfoSync(@NonNull int page, @NonNull String language);

        CallResult getSearchMovieInfoSync(@NonNull int page,@NonNull String name, @NonNull String language);

        CallResult getMovieInfoSync(@NonNull int id, @NonNull String language);

        CallResult getPosterImageSync(@NonNull int movieId, @NonNull String poster_path, @NonNull ImageView imgIcon);
    }
}
