package org.qcp.droid.qr.clock;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class QrClockWidget extends AppWidgetProvider {

    public static final String SHARED_PREFS = "QrClockPreferences";

    private static SharedPreferences prefs = null;
    private static Timer timer = null;

    private static void schedule(long delay, boolean force) {
        boolean do_update = force;

        if( timer != null ) {
            timer.cancel();
            timer.purge();
            timer = null;

            do_update = true;
        }

        if( do_update ) {
            // Log.d( "QrClockWidget", "Setting schedule at " + delay );
            long when = System.currentTimeMillis();
            when -= when % delay;
            when += delay;

            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {

                // private final String TAG = "QrClockTimerTask";

                @Override
                public void run() {
                    try {
                        QrDraw.INSTANCE.draw();
                    } catch( Exception ex ) {
                        // Log.d( TAG, ex.getLocalizedMessage(), ex );
                    }
                }

            }, new Date(when), delay);

            QrDraw.INSTANCE.draw();
        }
    }

    public static void schedule() {
        if( prefs != null ) {
            long delay = Long.valueOf(prefs.getString("updates_interval", "5000"));
            // Log.d( "QrClockWidget", "Request schedule at " + delay );

            schedule(delay, false);
        }
    }

    public void onUpdate(Context appContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Log.d( "QrClockWidget", "Update" );
        // Log.d( "QrClockWidget", appContext.toString() );

        prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
        QrDraw.INSTANCE.configure(prefs, appContext, appWidgetManager, appWidgetIds);

        schedule(Long.valueOf(prefs.getString("update_interval", "5000")), true);
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        if( timer != null ) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

}