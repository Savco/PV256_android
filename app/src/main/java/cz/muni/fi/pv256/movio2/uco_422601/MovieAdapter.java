package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by micha on 1. 11. 2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Object> mData;

    private static final int TYPE_GENRE = 0;
    private static final int TYPE_MOVIE = 1;
    private static final String DATE_FORMAT = "yyyyMMdd";

    private static final String TAG = MainActivity.class.getSimpleName();

    public MovieAdapter(Context context, List<Object> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (BuildConfig.LOGGING) Log.d(TAG, " creating ViewHolder, type:" + viewType);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch(viewType) {
            case TYPE_GENRE:
                Log.d(TAG, " creating ViewHolder, type:" + viewType);
                view = inflater.inflate(R.layout.single_genre, parent, false);

                return new GenreViewHolder(view);
            case TYPE_MOVIE:
                view = inflater.inflate(R.layout.single_movie, parent, false);
                return new MovieViewHolder(view, mContext);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof Movie ? TYPE_MOVIE : TYPE_GENRE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (BuildConfig.LOGGING) Log.d(TAG, "binding ViewHolder");
        int rowType = getItemViewType(position);
        switch(rowType){
            case TYPE_MOVIE:
                MovieViewHolder movieHolder = (MovieViewHolder) holder;
                Movie movie = (Movie) mData.get(position);
                movieHolder.title.setText(movie.getTitle());
                movieHolder.rating.setText(Float.toString(movie.getPopularity()));
                Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + movie.getBackdrop()).into(movieHolder.image);
                break;
            case TYPE_GENRE:
                GenreViewHolder genreHolder = (GenreViewHolder) holder;
                genreHolder.text.setText((String) mData.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public GenreViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.genre_name);
        }
    }

    public void dataUpdate(List<Object> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView rating;
        public TextView date;
        private RelativeLayout mLayout;

        public MovieViewHolder(View itemView, final Context context) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            image = (ImageView) itemView.findViewById(R.id.movie_image);
            rating = (TextView) itemView.findViewById(R.id.movie_rating);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.layout);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context != null) {
                        ((MainActivity) context).onMovieSelect(getAdapterPosition());
                    }
                }
            };
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick (View view) {
                    if(context != null) {
                        ((MainActivity) context).onMovieLongClick(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            };
            mLayout.setOnClickListener(clickListener);
            mLayout.setOnLongClickListener(longClickListener);
        }
    }
}

