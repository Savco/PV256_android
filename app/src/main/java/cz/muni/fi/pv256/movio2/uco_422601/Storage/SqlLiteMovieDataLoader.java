package cz.muni.fi.pv256.movio2.uco_422601.Storage;

import android.content.Context;

import java.util.List;

import javax.sql.DataSource;

import cz.muni.fi.pv256.movio2.uco_422601.Movie;

/**
 * Created by micha on 12/8/2017.
 */

public class SqlLiteMovieDataLoader extends AbstractDataLoader<List<Movie>> {
    private MovieManager mMovieManager;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;

    public SqlLiteMovieDataLoader(Context context, MovieManager movieManager, String selection, String[] selectionArgs,
                                  String groupBy, String having, String orderBy) {
        super(context);
        mMovieManager = movieManager;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

    @Override
    protected List<Movie> buildList() {
        List<Movie> movieList = mMovieManager.getFavouriteMovies();
        return movieList;
    }

    public void create(Movie movie) {
        new InsertTask(this).execute(movie);
    }

    public void delete(Movie movie) {
        new DeleteTask(this).execute(movie);
    }

    private class InsertTask extends ContentChangingTask<Movie, Void, Void> {
        InsertTask(SqlLiteMovieDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Movie... params) {
            mMovieManager.createMovie(params[0]);
            return (null);
        }
    }

    private class DeleteTask extends ContentChangingTask<Movie, Void, Void> {
        DeleteTask(SqlLiteMovieDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Movie... params) {
            mMovieManager.deleteMovie(params[0]);
            return (null);
        }
    }
}
