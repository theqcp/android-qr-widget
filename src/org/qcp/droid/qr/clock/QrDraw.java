package org.qcp.droid.qr.clock;

import java.util.ArrayList;
import java.util.Collection;

import org.qcp.qr.QrCode;
import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrEcc;
import org.qcp.qr.parts.QrMode;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

public class QrDraw {
    
    private static final int MODULE_COUNT = 21, QUIET_ZONE_MODULES = 3, BORDER_ZONE_MODULES = 1;
    private static final int MODULE_OFF = 0xFFFFFFFF, MODULE_ON = 0xFF000000;
    private static final int MODULE_DEFAULT_SIZE = 4;
    
    private static final String dateFormatString = "yyMMMdd kk:mm:ss";
    
    public static final QrDraw INSTANCE = new QrDraw();
    
    private static String getTime() {
        return DateFormat.format( dateFormatString, System.currentTimeMillis() ).toString().toUpperCase();
    }
    
    private static int[] toArray( Collection<Integer> integerCollection ) {
        int[] array = new int[integerCollection.size()];
        
        int i = 0;
        for( Integer datum: integerCollection ) {
            array[i++] = datum;
        }
        
        return array;
    }
    
    private final ArrayList<Integer> appWidgetIdArray = new ArrayList<Integer>();
    
    private int basePelWidth = (MODULE_COUNT + (QUIET_ZONE_MODULES * 2) + BORDER_ZONE_MODULES + 1);
    private AppWidgetManager appWidgetManager = null;
    private SharedPreferences prefs = null;
    private Context appContext = null;
    
    private QrDraw() {
        // TODO Auto-generated constructor stub
    }
    
    public void configure( SharedPreferences prefs, Context appContext, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {
        this.appWidgetIdArray.clear();
        
        for( int widgetId: appWidgetIds ) {
            this.appWidgetIdArray.add( widgetId );
        }
        
        this.appWidgetManager = appWidgetManager;
        this.appContext = appContext;
        
        this.prefs = prefs;
    }
    
    public void draw() {
        String timeMessage = QrDraw.getTime();
        
        int pelSize = Integer.valueOf( prefs.getString( "module_size", String.valueOf( MODULE_DEFAULT_SIZE ) ) );
        int pelWidth = pelSize * basePelWidth;
        int pelStart = pelSize * (BORDER_ZONE_MODULES + QUIET_ZONE_MODULES);
        
        QrCode code = QrCode.forMessage( QrMode.AlphaNumeric, QrEcc.Quartile, 1, timeMessage );
        
        RemoteViews views = new RemoteViews( appContext.getPackageName(), R.layout.widget );
        Bitmap bitmap = Bitmap.createBitmap( pelWidth, pelWidth, Config.ARGB_4444 );
        Canvas canvas = new Canvas( bitmap );
        
        Paint p = new Paint();
        p.setAntiAlias( true );
        
        p.setStyle( Style.FILL );
        p.setStrokeWidth( 1 );
        p.setColor( MODULE_ON );
        canvas.drawRect( new RectF( 0, 0, pelWidth, pelWidth ), p );
        
        p.setColor( MODULE_OFF );
        canvas.drawRect( new RectF( pelSize, pelSize, pelWidth - pelSize, pelWidth - pelSize ), p );
        
        p.setColor( MODULE_ON );
        Qr2dBitStream qr = code.getStream();
        int x, y;
        for( int v = 0; v < MODULE_COUNT; v++ ) {
            for( int u = 0; u < MODULE_COUNT; u++ ) {
                if( qr.getBitAt( u, v ) ) {
                    x = pelStart + (u * pelSize);
                    y = pelStart + (v * pelSize);
                    canvas.drawRect( new Rect( x, y, x + pelSize, y + pelSize ), p );
                }
            }
        }
        
        views.setImageViewBitmap( R.id.widget_imageview, bitmap );
        
        appWidgetManager.updateAppWidget( toArray( appWidgetIdArray ), views );
    }
}
