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

import java.util.List;

/**
 * Created by micha on 1. 11. 2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Object> mData;

    private static final int TYPE_GENRE = 0;
    private static final int TYPE_MOVIE = 1;
    private static final int TYPE_NO_DATA = 2;

    private static final String TAG = MainActivity.class.getSimpleName();

    public MovieAdapter(Context context, List<Object> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, " creating ViewHolder, type:" + viewType);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch(viewType) {
            case TYPE_GENRE:
                view = inflater.inflate(R.layout.single_genre, parent, false);
                return new GenreViewHolder(view);
            case TYPE_MOVIE:
                view = inflater.inflate(R.layout.single_movie, parent, false);
                return new MovieViewHolder(view, mContext);
            case TYPE_NO_DATA:
                view = inflater.inflate(R.layout.no_data, parent, false);
                return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof Movie ? TYPE_MOVIE : TYPE_GENRE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "binding ViewHolder");
        int rowType = getItemViewType(position);
        switch(rowType){
            case TYPE_MOVIE:
                MovieViewHolder movieHolder = (MovieViewHolder) holder;
                Movie movie = (Movie) mData.get(position);
                movieHolder.title.setText(movie.getTitle());
                movieHolder.rating.setText(Float.toString(movie.getPopularity()));
                if(!movie.getCoverPath().isEmpty() && mContext != null) {
                    movieHolder.image.setImageDrawable(mContext.getDrawable(Integer.parseInt(movie.getCoverPath())));
                }
                break;
            case TYPE_GENRE:
                GenreViewHolder genreHolder = (GenreViewHolder) holder;
                genreHolder.text.setText((String) mData.get(position));
                break;
            case TYPE_NO_DATA:
                EmptyViewHolder emptyView = (EmptyViewHolder) holder;
                emptyView.text.setText(mData.get(position).toString() /*noDataLabel*/);
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

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public TextView rating;
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
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.no_data_text);
        }
    }
}

