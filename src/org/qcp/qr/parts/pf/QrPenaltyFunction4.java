package org.qcp.qr.parts.pf;

import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrAbstractPenaltyFunction;

/**
 * Gives a penalty based on the ratio of dark to light pixels.
 */
public class QrPenaltyFunction4 extends QrAbstractPenaltyFunction {
    
    public static final QrPenaltyFunction4 INSTANCE = new QrPenaltyFunction4( "Penalty rule 4" );
    
    private QrPenaltyFunction4( String name ) {
        setName( name );
    }
    
    private int score( int set, int total ) {
        return (int) Math.abs( Math.floor( (((Double.valueOf( set ) / Double.valueOf( total )) * 100) - 50) / 5.0 ) * 10 );
    }
    
    @Override
    protected void run( Qr2dBitStream qrStream ) {
        int total = 0, set = 0;
        
        // Count pixels
        for( int v = 0, n = qrStream.getSideLength(); v < n; v++ ) {
            for( int u = 0; u < n; u++ ) {
                if( qrStream.getBitAt( u, v ) ) {
                    set++;
                }
                total++;
            }
        }
        
        addPoints( score( set, total ) );
    }
}