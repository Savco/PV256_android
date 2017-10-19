package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


/**
 * Created by micha on 12. 10. 2017.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener{

    private static final String PREFERENCES = "Preferencies";
    private static final String PRIMARY_THEME = "Primary";

    private SharedPreferences sharedPreferences;
    private boolean isPrimary;

    private Button mButtonSwitch;
    private boolean mTwoPane;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, " onStart method");
        // Cast kodu pre button na zmenu temy z 2. ulohy
//        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
//        isPrimary = sharedPreferences.getBoolean(PRIMARY_THEME, false);
//
//        if (isPrimary) {
//            setTheme(R.style.AppBaseTheme);
//        } else {
//            setTheme(R.style.AppCustomTheme);
//        }
//
//        setContentView(R.layout.activity_app);
//
//        mButtonSwitch = (Button) findViewById(R.id.button_send);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();
//        mButtonSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editor.putBoolean(PRIMARY_THEME, !isPrimary);
//                editor.apply();
//                Intent restart = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(restart);
//                finish();
//            }
//        });
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
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onMovieSelect(Movie movie) {
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

