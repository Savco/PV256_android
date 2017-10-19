package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by micha on 19. 10. 2017.
 */

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;

    private OnMovieSelectListener mListener;

    private ArrayList<Movie> mMovies;

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        try {
            mListener = (OnMovieSelectListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, "Activity must implement OnMovieSelectListener", e);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null; //Avoid leaking the Activity
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_names, container, false);

        mMovies = new ArrayList<>();
        mMovies.add(new Movie(getCurrentTime().getTime(), "", "Stopař Cory Lambert objeví uprostřed zasněžené pusté krajiny Wyomingu tělo mrtvé indiánské dívky. Vyšetřování se ujímá novopečená agentka FBI Jane Bannerová, která do odlehlé indiánské rezervace Wind River přijíždí z Las Vegas, zcela nepřipravená na drsné podnebí, jež tam panuje. Na pomoc s vyšetřováním si přizve stopaře Coryho a společně se snaží přijít na kloub záhadné smrti...", "Wind River", 4.5f));
        mMovies.add(new Movie(getCurrentTime().getTime(), "", "Svět Kingsmanů se poprvé představil ve filmu Kingsman: Tajná služba jako nezávislá, mezinárodní, špionážní agentura pracující v hlibokém utajení a jejímž cílem je bezpečný svět. V dalším pokračování Kingsman: Zlatý kruh čelí naši hrdinové novým výzvám. Centrála Kingsmanů je zničena a celý svět se stává rukojmím. Kingsmany čekají nové úkoly i překážky. Spojí se se spřátelenou americkou špionážní organizací Statesman. Obě dvě agentury musí společnými silami porazit nebezpečného nepřítele a zachránit tak svět.", "Kingsman: Zlatý kruh ", 3.5f));
        mMovies.add(new Movie(getCurrentTime().getTime(), "", "Harry Hole (Michael Fassbender) je enfant terrible týmu, vyšetřujícího zločiny v norské metropoli. Na jedné straně je to výjimečně dobrý detektiv, jehož netradiční metody skoro vždy vedou k úspěchu, na straně druhé se však jedná o nezodpovědného alkoholika s tolika prohřešky, že je už dál nechtějí přehlížet nejen jeho nadřízení, ale ani jeho přítelkyně Rachel (Charlotte Gainsbourg), s níž se právě rozešel. Jediné, co stojí mezi ním a pitím, je případ, který by ho přivedl na jiné myšlenky.", "Sněhulák ", 3.0f));

        mButton1 = (Button) view.findViewById(R.id.movie1);
        mButton2 = (Button) view.findViewById(R.id.movie2);
        mButton3 = (Button) view.findViewById(R.id.movie3);

        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.movie1:
                        mListener.onMovieSelect(mMovies.get(0));
                        break;
                    case R.id.movie2:
                        mListener.onMovieSelect(mMovies.get(1));
                        break;
                    case R.id.movie3:
                        mListener.onMovieSelect(mMovies.get(2));
                        break;
                }
            }
        };

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mButton1.setOnClickListener(clickListener);
        mButton2.setOnClickListener(clickListener);
        mButton3.setOnClickListener(clickListener);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return view;
    }

    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie);
    }

    private Date getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        return cal.getTime();
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

