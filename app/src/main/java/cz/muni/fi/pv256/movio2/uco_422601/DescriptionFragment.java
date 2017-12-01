package cz.muni.fi.pv256.movio2.uco_422601;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.support.annotation.Nullable;

import com.squareup.picasso.Picasso;

/**
 * Created by micha on 19. 10. 2017.
 */

public class DescriptionFragment extends Fragment {
    public static final String TAG = DescriptionFragment.class.getSimpleName();
    private static final String ARGS_MOVIE = "args_movie";

    private Context mContext;
    private Movie mMovie;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);

        TextView titleTv = (TextView) view.findViewById(R.id.detail_movie);
        TextView titleLowTv = (TextView) view.findViewById(R.id.detail_movie_low);
        ImageView posterImage = (ImageView) view.findViewById(R.id.poster_image);
        ImageView coverImage = (ImageView) view.findViewById(R.id.cover_image);

        if (mMovie != null) {
            titleTv.setText(mMovie.getTitle());
            titleLowTv.setText("To be done later");
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mMovie.getBackdrop()).into(posterImage);
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mMovie.getCoverPath()).into(coverImage);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, " onAttach method");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, " onStart method");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, " onResume method");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, " onPause method");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, " onStop method");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " onDestroy method");
    }
}
