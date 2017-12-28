package cz.muni.fi.pv256.movio2.uco_422601;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by micha on 19. 10. 2017.
 */

public class Movie implements Parcelable{

    private Long mId;
    private long mRealeaseDate;
    private String mCoverPath;
    private String mTitle;
    private String mBackdrop;
    private String mOverview;
    private float mPopularity;

    public Movie(){
    }

    public Movie(Long id, long realeaseDate, String coverPath, String backdrop, String title, float popularity, String overview) {
        mId = id;
        mRealeaseDate = realeaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mOverview = overview;
        mPopularity = popularity;
    }

    public Long getId() { return mId; }

    public void setId(Long id) { mId = id; }

    public long getRealeaseDate() {
        return mRealeaseDate;
    }

    public void setRealeaseDate(long realeaseDate) {
        mRealeaseDate = realeaseDate;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public void setCoverPath(String coverPath) {
        mCoverPath = coverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public void setBackdrop(String backdrop) {
        mBackdrop = backdrop;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public void setOverview(String overview){ mOverview = overview;}

    public String getOverview() { return  mOverview;}

    public Movie(Parcel in) {
        mId = in.readLong();
        mRealeaseDate = in.readLong();
        mCoverPath = in.readString();
        mTitle = in.readString();
        mBackdrop = in.readString();
        mPopularity = in.readFloat();
        mOverview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mRealeaseDate);
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeString(mBackdrop);
        dest.writeFloat(mPopularity);
        dest.writeString(mOverview);
    }

    @Override
    public String toString() {
        return mTitle + " " + mRealeaseDate + " " + mPopularity;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) { return new Movie[size]; }
    };
}