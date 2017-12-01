package cz.muni.fi.pv256.movio2.uco_422601;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by micha on 11/29/2017.
 */

public class MovieDownloadService extends IntentService {

    public static final String TAG = MovieDownloadService.class.getSimpleName();
    public static final String ACTION = "download";
    public static final String POPULAR = "popular";
    public static final String BEST90 = "best90";
    public static final String ERROR = "error";
    public static final String NO_ERROR = "no error";
    public static final String CONNECTION_ERROR = "connection error";

    public MovieDownloadService() {
        super(TAG);
    }

    public MovieDownloadService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION);

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieAPI service = retrofit.create(MovieAPI.class);

            startDownloadNotification();
            Call<MovieList> requestPop = service.getPopularMovies();
            Call<MovieList> requestBest = service.getBestMoviesIn90s();

            MovieList popularMovies = requestPop.execute().body();
            MovieList best90Movies = requestBest.execute().body();

            broadcastIntent.putExtra(POPULAR, new ArrayList<MovieDTO>(popularMovies.getResults()));
            broadcastIntent.putExtra(BEST90, new ArrayList<MovieDTO>(best90Movies.getResults()));
            broadcastIntent.putExtra(ERROR, NO_ERROR);

            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
            doneDownloadNotification();
        } catch (IOException e) {
            e.printStackTrace();
            broadcastIntent.putExtra(ERROR, CONNECTION_ERROR);
            sendBroadcast(broadcastIntent);
        }
    }

    private void startDownloadNotification() {
        Intent intent = new Intent(this, MainFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Starting download")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private void doneDownloadNotification() {
        Intent intent = new Intent(this, MainFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Download done")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }
}