package cz.muni.fi.pv256.movio2.uco_422601;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by micha on 11/29/2017.
 */

public interface MovieAPI {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @GET("3/discover/movie?api_key=" + Constants.API_KEY + "&sort_by=popularity.desc")
    Call<MovieList> getPopularMovies();

    @GET("3/discover/movie?api_key=" + Constants.API_KEY + "&primary_release_date.gte=1990-01-01&primary_release_date.lte=1999-12-24")
    Call<MovieList> getBestMoviesIn90s();

    @GET("3/movie/{id}?api_key=" + Constants.API_KEY)
    Call<MovieDTO> getMovieById(@Path("id") Long id);
}

