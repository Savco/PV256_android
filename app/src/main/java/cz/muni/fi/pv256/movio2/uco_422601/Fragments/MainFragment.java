package cz.muni.fi.pv256.movio2.uco_422601.Fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.SwitchCompat;
import android.view.ViewStub;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422601.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_422601.Movie;
import cz.muni.fi.pv256.movio2.uco_422601.MovieAdapter;
import cz.muni.fi.pv256.movio2.uco_422601.MovieDTO;
import cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService;
import cz.muni.fi.pv256.movio2.uco_422601.R;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieDbHelper;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieManager;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.SqlLiteMovieDataLoader;

import static cz.muni.fi.pv256.movio2.uco_422601.MainActivity.mData;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.ACTION;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.BEST90;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.CHANNEL;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.ERROR;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.NO_ERROR;
import static cz.muni.fi.pv256.movio2.uco_422601.Networking.MovieDownloadService.POPULAR;

/**
 * Created by micha on 19. 10. 2017.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>{

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;

    private RecyclerView mRecyclerView;
    private ViewStub mEmptyView;
    private MovieAdapter mMovieAdapter;
    private OnMovieSelectListener mListener;
    private MovieDownloadReceiver mReceiver;

    private static final int LOADER_ID = 1;
    private SQLiteDatabase mDatabase;
    private MovieManager mMovieManager;
    private MovieDbHelper mDbHelper;
    private boolean isFavourite;
    private MainFragment fragmentToCreate;
    private SwitchCompat switchButton;

    public MainFragment() {}

    public static MainFragment newInstance(Boolean isChecked) {
        Bundle args = new Bundle();
        args.putBoolean("isFavorite", isChecked);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFavourite = getArguments().getBoolean("isFavorite") ? true : false;
        }
        else {
            isFavourite = false;
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mListener = (OnMovieSelectListener) activity;
        } catch (ClassCastException e) {
            if (BuildConfig.LOGGING) Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null; //Avoid leaking the Activity
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_names, container, false);
        mEmptyView = (ViewStub) view.findViewById(R.id.empty);


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initChannels(getActivity());

        mMovieAdapter = new MovieAdapter(getContext(), new ArrayList<Object>());
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (isFavourite) {
            mDbHelper = new MovieDbHelper(getActivity());
            mDatabase = mDbHelper.getWritableDatabase();
            mMovieManager = new MovieManager(mDatabase);
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            if(!isConnected(this.getActivity())) {
                Toast.makeText(getActivity(), "NO CONNECTION", Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(getActivity(), MovieDownloadService.class);
                getActivity().startService(intent);

                IntentFilter intentFilter = new IntentFilter(ACTION);
                mReceiver = new MovieDownloadReceiver();
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
            }
        }
      return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        SqlLiteMovieDataLoader loader = new SqlLiteMovieDataLoader(this.getActivity(), mMovieManager, null, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mData.clear();
        for (Movie movie : data) {
            mData.add(movie);
        }
        mMovieAdapter.dataUpdate(mData);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    public interface OnMovieSelectListener {
        void onMovieSelect(int position);
    }

    public interface OnMovieLongClickListener {
        void onMovieLongClick(int position);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.LOGGING) Log.d(TAG, " onStart method");
    }
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("MovieDownloadService");
        mReceiver = new MovieDownloadReceiver();
        getActivity().registerReceiver(mReceiver, intentFilter);
        if (BuildConfig.LOGGING) Log.d(TAG, " onResume method");
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
        if (BuildConfig.LOGGING) Log.d(TAG, " onPause method");
    }
    @Override
    public void onStop() {
        super.onStop();
        if (BuildConfig.LOGGING) Log.d(TAG, " onStop method");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildConfig.LOGGING) Log.d(TAG, " onDestroy method");
    }

    public class MovieDownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String error = intent.getStringExtra(ERROR);
            mData.clear();
            if(error == NO_ERROR) {
                ArrayList<MovieDTO> popularMovieList = (ArrayList<MovieDTO>)intent.getSerializableExtra(POPULAR);
                ArrayList<MovieDTO> best90MovieList = (ArrayList<MovieDTO>)intent.getSerializableExtra(BEST90);

                mData.add("Popular");
                addMovies(popularMovieList);
                mData.add("Best in the 90s");
                addMovies(best90MovieList);

                if (mMovieAdapter != null) mMovieAdapter.dataUpdate(mData);
            }
            else {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }
        }

        private void addMovies(ArrayList<MovieDTO> movieList){
            for (MovieDTO m : movieList) {
                Movie movie = new Movie(Long.parseLong(m.getId(), 10), m.getRealeaseDateAsLong(), m.getCoverPath(), m.getBackdrop(), m.getTitle(), m.getPopularityAsFloat(), m.getOverview());
                mData.add(movie);
            }
        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL,
                "Download",
                NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
    }
}

