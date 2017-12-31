package cz.muni.fi.pv256.movio2.uco_422601.Sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by micha on 12/29/2017.
 */

public class MovieSyncBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdaterSyncAdapter.getSyncAccount(context);
    }
}
