package cz.muni.fi.pv256.movio2.uco_422601.Fragments;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import cz.muni.fi.pv256.movio2.uco_422601.BuildConfig;
import cz.muni.fi.pv256.movio2.uco_422601.Movie;
import cz.muni.fi.pv256.movio2.uco_422601.R;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieDbHelper;
import cz.muni.fi.pv256.movio2.uco_422601.Storage.MovieManager;

/**
 * Created by micha on 19. 10. 2017.
 */

public class DescriptionFragment extends Fragment {
    public static final String TAG = DescriptionFragment.class.getSimpleName();
    private static final String ARGS_MOVIE = "args_movie";

    private Context mContext;
    private Movie mMovie;

    private SQLiteDatabase mDatabase;
    private MovieManager mMovieManager;
    private MovieDbHelper mDbHelper;
    private FloatingActionButton floatingActionButton;
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static DescriptionFragment newInstance(Movie movie) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (BuildConfig.LOGGING) Log.d(TAG, " onCreate method");
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            mMovie = args.getParcelable(ARGS_MOVIE);
            Log.d(TAG, "movienotempty");
        }
        else {
            mMovie = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (mMovie == null) {
            view = inflater.inflate(R.layout.empty_description, container, false);
            return  view;
        }
        else {
            mDbHelper = new MovieDbHelper(getActivity());
            mDatabase = mDbHelper.getWritableDatabase();
            mMovieManager = new MovieManager(mDatabase);

            Log.d(TAG, mMovie.toString());
            view = inflater.inflate(R.layout.fragment_description, container, false);

            floatingActionButton = view.findViewById(R.id.add);
            if (mMovieManager.containsId(mMovie.getId())) {
                floatingActionButton.setImageResource(R.drawable.minus);
            }

            floatingActionButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (BuildConfig.LOGGING) Log.d(TAG, "onClickFloatingButton called");
                    if (mMovieManager.containsId(mMovie.getId())) {
                        mMovieManager.deleteMovie(mMovie);
                        Toast.makeText(getActivity(), mMovie.getTitle() + " removed from database.", Toast.LENGTH_SHORT).show();
                        floatingActionButton.setImageResource(R.drawable.ic_plus_sign);
                    } else {
                        mMovieManager.createMovie(mMovie);
                        Toast.makeText(getActivity(), mMovie.getTitle() + " added to database.", Toast.LENGTH_SHORT).show();
                        floatingActionButton.setImageResource(R.drawable.minus);
                    }
                }
            });

            TextView titleTv = (TextView) view.findViewById(R.id.detail_movie);
            TextView titleLowTv = (TextView) view.findViewById(R.id.detail_movie_low);
            titleLowTv.setMovementMethod(new ScrollingMovementMethod());
            TextView date = (TextView) view.findViewById(R.id.date);
            ImageView posterImage = (ImageView) view.findViewById(R.id.poster_image);
            ImageView coverImage = (ImageView) view.findViewById(R.id.cover_image);
            Log.d(TAG, mMovie.toString());

            if (mMovie != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                date.setText("Premiere    " + formatter.format(mMovie.getRealeaseDate()));
                titleTv.setText(mMovie.getTitle());
                titleLowTv.setText(mMovie.getOverview());
                Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mMovie.getBackdrop()).into(posterImage);
                Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mMovie.getCoverPath()).into(coverImage);
            }
            //mDatabase.close();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (BuildConfig.LOGGING) Log.d(TAG, " onAttach method");
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
