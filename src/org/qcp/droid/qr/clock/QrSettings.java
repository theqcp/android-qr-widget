package org.qcp.droid.qr.clock;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

public class QrSettings extends PreferenceActivity {
    
    // private final static String TAG = "QrSettings";
    
    private int mAppWidgetId = 0;
    
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource( R.xml.preferences );
    }
    
    @Override
    public void onBackPressed() {
        // Log.d( TAG, "onBackPressed" );
        
        Bundle extras = getIntent().getExtras();
        
        if( extras != null ) {
            mAppWidgetId = extras.getInt( AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID );
        }
        
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance( getBaseContext() );
        RemoteViews views = new RemoteViews( getBaseContext().getPackageName(), R.layout.widget );
        
        appWidgetManager.updateAppWidget( mAppWidgetId, views );
        
        Intent resultValue = new Intent();
        resultValue.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId );
        setResult( RESULT_OK, resultValue );
        
        QrClockWidget.schedule();
        
        finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        menu.add( Menu.NONE, 0, 0, "About" );
        return super.onCreateOptionsMenu( menu );
    }
    
    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch( item.getItemId() ) {
            case 0:
                startActivity( new Intent( this, QrAbout.class ) );
                return true;
        }
        return false;
    }
    
}
