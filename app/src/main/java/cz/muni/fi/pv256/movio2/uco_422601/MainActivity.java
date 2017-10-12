package cz.muni.fi.pv256.movio2.uco_422601;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


/**
 * Created by micha on 12. 10. 2017.
 */

public class MainActivity extends AppCompatActivity{

    private static final String PREFERENCES = "Preferencies";
    private static final String PRIMARY_THEME = "Primary";

    private SharedPreferences sharedPreferences;
    private boolean isPrimary;

    private Button mButtonSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        isPrimary = sharedPreferences.getBoolean(PRIMARY_THEME, false);

        if(isPrimary){
            setTheme(R.style.AppBaseTheme);
        } else {
            setTheme(R.style.AppCustomTheme);
        }

        setContentView(R.layout.activity_app);

        mButtonSwitch = (Button)findViewById(R.id.button_send);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        mButtonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(PRIMARY_THEME, !isPrimary);
                editor.apply();
                Intent restart = new Intent(getApplicationContext() ,MainActivity.class);
                startActivity(restart);
                finish();
            }
        });
    }
}
