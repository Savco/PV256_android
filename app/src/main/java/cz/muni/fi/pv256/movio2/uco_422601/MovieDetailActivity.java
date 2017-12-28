package cz.muni.fi.pv256.movio2.uco_422601;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import cz.muni.fi.pv256.movio2.uco_422601.Fragments.DescriptionFragment;

/**
 * Created by micha on 19. 10. 2017.
 */

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "extra_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState == null){
            Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            FragmentManager fm = getSupportFragmentManager();
            DescriptionFragment fragment = (DescriptionFragment) fm.findFragmentById(R.id.movie_detail_container);

            if (fragment == null) {
                fragment = DescriptionFragment.newInstance(movie);
                fm.beginTransaction()
                        .add(R.id.movie_detail_container, fragment)
                        .commit();
            }
        }
    }
}
