package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewStub;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import cz.muni.fi.pv256.movio2.uco_422601.BuildConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by micha on 12. 10. 2017.
 */

public class MainActivity extends AppCompatActivity implements MainFragment.OnMovieSelectListener, MainFragment.OnMovieLongClickListener{

    private static final String PREFERENCES = "Preferencies";
    private static final String PRIMARY_THEME = "Primary";

    private SharedPreferences sharedPreferences;
    private boolean isPrimary;

    private Button mButtonSwitch;
    private boolean mTwoPane;
    private List<Object> mData;

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
            getSupportActionBar().setElevation(0f);
        }

        mData = new ArrayList<Object>();

        mData.add("Thriller");
        for (int i = 0; i < 2; i++) {
            mData.add(new Movie(getCurrentTime().getTime(), Integer.toString(R.drawable.windriver), "Stopař Cory Lambert objeví uprostřed zasněžené pusté krajiny Wyomingu tělo mrtvé indiánské dívky. Vyšetřování se ujímá novopečená agentka FBI Jane Bannerová, která do odlehlé indiánské rezervace Wind River přijíždí z Las Vegas, zcela nepřipravená na drsné podnebí, jež tam panuje. Na pomoc s vyšetřováním si přizve stopaře Coryho a společně se snaží přijít na kloub záhadné smrti...", "Wind River", 4.5f));
            mData.add(new Movie(getCurrentTime().getTime(), Integer.toString(R.drawable.snowman), "Harry Hole (Michael Fassbender) je enfant terrible týmu, vyšetřujícího zločiny v norské metropoli. Na jedné straně je to výjimečně dobrý detektiv, jehož netradiční metody skoro vždy vedou k úspěchu, na straně druhé se však jedná o nezodpovědného alkoholika s tolika prohřešky, že je už dál nechtějí přehlížet nejen jeho nadřízení, ale ani jeho přítelkyně Rachel (Charlotte Gainsbourg), s níž se právě rozešel. Jediné, co stojí mezi ním a pitím, je případ, který by ho přivedl na jiné myšlenky.", "Sněhulák ", 3.0f));
        }
        mData.add("Action");
        for (int i = 0; i < 2; i++) {
            mData.add(new Movie(getCurrentTime().getTime(), Integer.toString(R.drawable.kingsman), "Svět Kingsmanů se poprvé představil ve filmu Kingsman: Tajná služba jako nezávislá, mezinárodní, špionážní agentura pracující v hlibokém utajení a jejímž cílem je bezpečný svět. V dalším pokračování Kingsman: Zlatý kruh čelí naši hrdinové novým výzvám. Centrála Kingsmanů je zničena a celý svět se stává rukojmím. Kingsmany čekají nové úkoly i překážky. Spojí se se spřátelenou americkou špionážní organizací Statesman. Obě dvě agentury musí společnými silami porazit nebezpečného nepřítele a zachránit tak svět.", "Kingsman: Zlatý kruh ", 3.5f));
        }

        ViewStub emptyView = (ViewStub) findViewById(R.id.empty);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        MovieAdapter adapter = new MovieAdapter(this, mData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mData.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMovieSelect(int position) {
        Movie movie = (Movie)mData.get(position);
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

    private Date getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        return cal.getTime();
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
}

