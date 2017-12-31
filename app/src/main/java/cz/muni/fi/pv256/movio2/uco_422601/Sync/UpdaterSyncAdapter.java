package cz.muni.fi.pv256.movio2.uco_422601.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import cz.muni.fi.pv256.movio2.uco_422601.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_422601.Constants;
import cz.muni.fi.pv256.movio2.uco_422601.Movie;
import cz.muni.fi.pv256.movio2.uco_422601.MovieAPI;
import cz.muni.fi.pv256.movio2.uco_422601.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422601.R;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieDbHelper;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieManager;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.CHANNEL;

/**
 * Created by micha on 12/28/2017.
 */

public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {

    // Interval at which to sync with the server, in seconds.
    public static final int SYNC_INTERVAL = 60 * 60 * 24; //day
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private static final String TAG = UpdaterSyncAdapter.class.getSimpleName();

    public UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY) //enter non null Bundle, otherwise on some phones it crashes sync
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        if (BuildConfig.LOGGING) Log.d(TAG, " Sync immediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one if the
     * fake account doesn't exist yet.  If we make a new account, we call the onAccountCreated
     * method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (BuildConfig.LOGGING) Log.d(TAG, "Starting synchronization...");
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieAPI service = retrofit.create(MovieAPI.class);

            MovieDbHelper dbHelper = new MovieDbHelper(getContext());
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            MovieManager movieManager = new MovieManager(database);

            List<Movie> favouriteFilms = movieManager.getFavouriteMovies();
            for (int i = 0; i < favouriteFilms.size(); i++) {
                Movie movie = favouriteFilms.get(i);
                Call<MovieDTO> request = service.getMovieById(movie.getId());
                MovieDTO requestMovie = request.execute().body();
                Movie updatedMovie = toMovie(requestMovie);

                if (!compareMovies(movie, updatedMovie)) {
                    movieManager.deleteMovie(movie);
                    movieManager.createMovie(updatedMovie);
                    Notification.Builder n;
                    if (Build.VERSION.SDK_INT < 26) {
                        n  = new Notification.Builder(getContext());
                    }
                    else {
                        n = new Notification.Builder(getContext(), CHANNEL);
                    }
                    n.setContentTitle("Movio2").setAutoCancel(true)
                            .setContentText("Movie " + updatedMovie.getTitle() + " updated")
                            .setSmallIcon(R.mipmap.ic_launcher);

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(0, n.build());
                }
            }
            database.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (BuildConfig.LOGGING) Log.d(TAG, "Synchronization EXCEPTION");

        }
    }

    private boolean compareMovies(Movie oldMovie, Movie newMovie){
        if (oldMovie.getBackdrop().compareTo(newMovie.getBackdrop()) !=0 ||
            oldMovie.getOverview().compareTo(newMovie.getOverview()) !=0 ||
            oldMovie.getTitle().compareTo(newMovie.getTitle()) !=0 ||
            oldMovie.getCoverPath().compareTo(newMovie.getCoverPath()) !=0 ||
            Math.abs(oldMovie.getPopularity() - newMovie.getPopularity()) > 0.000001 ||
            oldMovie.getRealeaseDate() != newMovie.getRealeaseDate()) return false;
        else return true;
    }

    private Movie toMovie(MovieDTO m){
        Movie movie = new Movie(Long.parseLong(m.getId(), 10), m.getRealeaseDateAsLong(), m.getCoverPath(), m.getBackdrop(), m.getTitle(), m.getPopularityAsFloat(), m.getOverview());
        return movie;
    }
}