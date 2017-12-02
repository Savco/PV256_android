package cz.muni.fi.pv256.movio2.uco_422601;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

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
    public static final String CHANNEL = "default";

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

            if(!isConnected(this)) {
                Toast.makeText(this, "NO CONNECTION", Toast.LENGTH_LONG).show();
                broadcastIntent.putExtra(ERROR, CONNECTION_ERROR);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                errorDownloadNotification();
            }
            else {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            broadcastIntent.putExtra(ERROR, CONNECTION_ERROR);
            sendBroadcast(broadcastIntent);
        }
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void errorDownloadNotification() {
        Notification.Builder n = prepareNotification();
        n.setContentText("Error downloading data").setSmallIcon(R.mipmap.ic_error_download);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private void startDownloadNotification() {
        Notification.Builder n = prepareNotification();
        n.setContentText("Starting download").setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private void doneDownloadNotification() {
        Notification.Builder n = prepareNotification();
        n.setContentText("Download done").setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n.build()); //0 udává číslo notifikace. Na některých zařízeních nefunguje jinačí int než 0.
    }

    private Notification.Builder prepareNotification() {
        Intent intent = new Intent(this, MainFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder n;
        if (Build.VERSION.SDK_INT < 26) {
            n  = new Notification.Builder(this);
        }
        else {
            n = new Notification.Builder(this, CHANNEL);
        }
        n.setContentTitle(getResources().getString(R.string.app_name))
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        return n;
    }
}