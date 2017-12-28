package cz.muni.fi.pv256.movio2.uco_422601.Storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.pv256.movio2.uco_422601.Movie;

/**
 * Created by micha on 12/6/2017.
 */

public class MovieManager {
    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_RELEASE_DATE = 2;
    public static final int COL_MOVIE_RATING = 3;
    public static final int COL_MOVIE_COVER_PATH = 4;
    public static final int COL_MOVIE_BACKDROP_PATH = 5;
    public static final int COL_MOVIE_OVERVIEW = 6;

    SQLiteDatabase mDatabase;

    private static final String[] FILM_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_COVER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
    };

    public MovieManager(SQLiteDatabase database) {
        mDatabase = database;
    }

    public boolean createMovie(Movie movie) {
        if (movie == null) {
            throw new NullPointerException("movie == null");
        }
        if (movie.getTitle() == null) {
            throw new IllegalStateException("movie title cannot be null");
        }
        if (movie.getBackdrop() == null) {
            throw new IllegalStateException("movie overview cannot be null");
        }
        if (movie.getCoverPath() == null) {
            throw new IllegalStateException("movie coverpath cannot be null");
        }

        long result = mDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, prepareMovieValues(movie));
        return result != -1;
    }

    public List<Movie> getFavouriteMovies() {
        Cursor cursor = mDatabase.query(MovieContract.MovieEntry.TABLE_NAME, FILM_COLUMNS, null,
                null, null, null, null);
        List<Movie> movies = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                movies.add(getMovie(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return movies;
    }

    private Movie getMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getLong(COL_MOVIE_ID));
        movie.setTitle(cursor.getString(COL_MOVIE_TITLE));
        movie.setRealeaseDate(MovieContract.getDateFromDb(cursor.getString(COL_MOVIE_RELEASE_DATE)));
        movie.setCoverPath(cursor.getString(COL_MOVIE_COVER_PATH));
        movie.setPopularity(cursor.getFloat(COL_MOVIE_RATING));
        movie.setOverview(cursor.getString(COL_MOVIE_OVERVIEW));
        movie.setBackdrop(cursor.getString(COL_MOVIE_BACKDROP_PATH));

        return movie;
    }

    public boolean deleteMovie(Movie movie) {
        if (movie == null) {
            return false;
        }
        if (movie.getId() == null) {
            throw new IllegalStateException("movie id cannot be null");
        }

        int result = mDatabase.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry._ID + " = " + movie.getId(), null);
        return result != 0;
    }

    public boolean containsId(Long id) {
        Log.d("FilmDetailFragment", "id je ->" + id );
        String Query = "Select * from " + MovieContract.MovieEntry.TABLE_NAME + " where " + MovieContract.MovieEntry._ID + " = " + id;
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private ContentValues prepareMovieValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.insertDateToDb(movie.getRealeaseDate()));
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getPopularity());
        values.put(MovieContract.MovieEntry.COLUMN_COVER_PATH, movie.getCoverPath());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop());

        return values;
    }
}
