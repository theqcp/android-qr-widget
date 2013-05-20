package org.qcp.droid.qr.clock;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class QrAbout extends Activity {
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.qr_about );
        
        TextView text = (TextView) findViewById( R.id.aboutText );
        
        text.setText( Html.fromHtml( getString( R.string.qr_about_text ) ) );
    }
    
}
