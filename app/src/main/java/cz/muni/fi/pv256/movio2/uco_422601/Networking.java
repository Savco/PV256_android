package cz.muni.fi.pv256.movio2.uco_422601;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.pv256.movio2.uco_422601.Constants.API_KEY;

/**
 * Created by micha on 11/22/2017.
 */

public class Networking {

    private static String url = "http://api.themoviedb.org/";
    private static String call = "3/discover/movie?api_key=";

    private Networking() {}

    public static List<Movie> getPopularMovies() throws IOException {
        String filter = "&sort_by=popularity.desc";
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url + call + API_KEY + filter)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return getData(response.body().string());
    }

    public static List<Movie> getBestMoviesIn90s() throws IOException {
        String filter = "&primary_release_date.gte=1990-01-01&primary_release_date.lte=1999-12-24";
        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url + call + API_KEY + filter)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        return getData(response.body().string());
    }

    private static ArrayList<Movie> getData(String data) {
        ArrayList<Movie> mData = new ArrayList();

        try {
            JSONObject json = new JSONObject(data);
            Gson gson = new Gson();
            ArrayList<MovieDTO> movies = gson.fromJson(json.getJSONArray("results").toString(), new TypeToken<List<MovieDTO>>() {
            }.getType());

            for (MovieDTO m : movies) {
                Movie movie = new Movie(m.getRealeaseDateAsLong(), m.getCoverPath(), m.getBackdrop(), m.getTitle(), m.getPopularityAsFloat());
                mData.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList();
        }
        return mData;
    }
}
