package cz.muni.fi.pv256.movio2.uco_422601;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by micha on 11/23/2017.
 */

public class MovieDTO implements Serializable {

    @SerializedName("id")
    private String mId;
    @SerializedName("release_date")
    private String mRealeaseDate;
    @SerializedName("poster_path")
    private String mCoverPath;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("backdrop_path")
    private String mBackdrop;
    @SerializedName("vote_average")
    private String mPopularity;
    @SerializedName("overview")
    private String mOverview;

    public MovieDTO(String id, String realeaseDate, String coverPath, String backdrop, String title, String popularity, String overview) {
        mId = id;
        mRealeaseDate = realeaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mPopularity = popularity;
        mOverview = overview;
    }

    public String getRealeaseDate() {
        return mRealeaseDate;
    }

    public long getRealeaseDateAsLong() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = (Date)formatter.parse(getRealeaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public String getId() {
        return mId;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public Float getPopularityAsFloat() {
        return Float.parseFloat(getPopularity());
    }

    public String getOverview() {return mOverview;}
}
