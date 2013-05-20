package org.qcp.qr.parts.pf;

import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrAbstractPenaltyFunction;

/**
 * Gives a penalty of 3 for each 2x2 block of like bits.
 */
public class QrPenaltyFunction2 extends QrAbstractPenaltyFunction {
    
    public static final QrPenaltyFunction2 INSTANCE = new QrPenaltyFunction2( "Penalty rule 2" );
    
    private QrPenaltyFunction2( String name ) {
        setName( name );
    }
    
    @Override
    protected void run( Qr2dBitStream qrStream ) {
        int penalty = 3, sum;
        
        // Look for squares
        for( int v = 0, n = qrStream.getSideLength() - 1; v < n; v++ ) {
            for( int u = 0; u < n; u++ ) {
                sum = 0;
                for( int j = 0; j < 2; j++ ) {
                    for( int i = 0; i < 2; i++ ) {
                        if( qrStream.getBitAt( u + i, v + j ) ) {
                            sum++;
                        }
                    }
                }
                if( (sum == 0) || (sum == 4) ) {
                    addPoints( penalty );
                }
            }
        }
    }
    
}