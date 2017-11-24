package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Debug;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static cz.muni.fi.pv256.movio2.uco_422601.MainActivity.mData;

/**
 * Created by micha on 19. 10. 2017.
 */

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;

    private RecyclerView mRecyclerView;
    private ViewStub mEmptyView;
    private MovieAdapter mMovieAdapter;
    private OnMovieSelectListener mListener;
    private DownloadingTask mDownloadingTask;

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

        if(!isConnected(this.getActivity())) {
            Toast.makeText(getActivity(), "NO CONNECTION", Toast.LENGTH_LONG).show();
        }
        else {
            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            mMovieAdapter = new MovieAdapter(getContext(), new ArrayList<Object>());
            mRecyclerView.setAdapter(mMovieAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mDownloadingTask = new DownloadingTask();
            mDownloadingTask.execute();
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

    private class DownloadingTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "doInBackground - thread: " + Thread.currentThread().getName());
            //if mData.isEmpty()
            try {
                List<Movie> popular = Networking.getPopularMovies();
                List<Movie> old = Networking.getBestMoviesIn90s();
                mData.add("Popular");
                mData.addAll(popular);
                mData.add("Best in the 90s");
                mData.addAll(old);
                MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMovieAdapter.dataUpdate(mData);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getName());
            if (result)
                Toast.makeText(getActivity().getApplicationContext(), "Data sucesfully downloaded", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity().getApplicationContext(), "Data not recieved", Toast.LENGTH_SHORT).show();

            if (mData.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
            mDownloadingTask = null;
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "onCancelled - thread: " + Thread.currentThread().getName());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (BuildConfig.LOGGING) Log.d(TAG, " onStart method");
    }
    @Override
    public void onResume() {
        super.onResume();
        if (BuildConfig.LOGGING) Log.d(TAG, " onResume method");
    }
    @Override
    public void onPause() {
        super.onPause();
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
}

