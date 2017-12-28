package cz.muni.fi.pv256.movio2.uco_422601;

import android.os.Debug;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cz.muni.fi.pv256.movio2.uco_422601.Fragments.DescriptionFragment;
import cz.muni.fi.pv256.movio2.uco_422601.Fragments.MainFragment;

/**
 * Created by micha on 12. 10. 2017.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener, MainFragment.OnMovieLongClickListener {

    private static final String PREFERENCES = "Preferencies";
    private static final String PRIMARY_THEME = "Primary";

    private SharedPreferences sharedPreferences;
    private boolean isPrimary;

    private Button mButtonSwitch;
    private boolean mTwoPane;
    public static List<Object> mData;
    private SwitchCompat mSwitchButton;
    private Toolbar toolbar;
    protected MainFragment fragmentToCreate;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, " onStart method");

        setContentView(R.layout.activity_app);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DescriptionFragment(), DescriptionFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            //getSupportActionBar().setElevation(0f);
        }

        mData = new ArrayList<Object>();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
    }

    @Override
    public void onMovieSelect(int position) {
        Movie movie = (Movie) mData.get(position);
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();

            DescriptionFragment fragment = DescriptionFragment.newInstance(movie);
            fm.beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DescriptionFragment.TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onMovieLongClick(int position) {
        Toast.makeText(this, ((Movie) mData.get(position)).getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.menu, menu);
                MenuItem item = menu.findItem(R.id.menuSwitch);
                item.setActionView(R.layout.menu_switch);
                mSwitchButton = item.getActionView().findViewById(R.id.switchForActionBar);
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (compoundButton.isChecked()) {
                                compoundButton.setText("Favourites");
                                compoundButton.setChecked(true);
                                fragmentToCreate = MainFragment.newInstance(true);
                            } else {
                                compoundButton.setText("Discover");
                                compoundButton.setChecked(false);
                              fragmentToCreate = MainFragment.newInstance(false);
                            }
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_main, fragmentToCreate)
                            .commit();
            }
        });
                return true;
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}