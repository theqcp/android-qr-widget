package org.qcp.qr.parts.pf;

import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrAbstractPenaltyFunction;

/**
 * Gives a penalty if five or more of the same colored pixels are next to each
 * other in a row or column. For the first five consecutive pixels, the penalty
 * score is increased by 3. Each consecutive pixel after that adds 1 to the
 * penalty.
 */
public class QrPenaltyFunction1 extends QrAbstractPenaltyFunction {
    
    public static final QrPenaltyFunction1 INSTANCE = new QrPenaltyFunction1( "Penalty rule 1" );
    
    private QrPenaltyFunction1( String name ) {
        setName( name );
    }
    
    @Override
    protected void run( Qr2dBitStream qrStream ) {
        boolean lastBit = false, bit = false;
        int count = 0;
        
        // Count pixels horizontally
        for( int v = 0, n = qrStream.getSideLength(); v < n; v++ ) {
            for( int u = 0; u < n; u++ ) {
                if( u == 0 ) {
                    lastBit = qrStream.getBitAt( u, v );
                    bit = lastBit;
                    count = 1;
                } else {
                    bit = qrStream.getBitAt( u, v );
                    if( bit == lastBit ) {
                        count++;
                    } else {
                        if( count > 4 ) {
                            addPoints( count - 2 );
                        }
                        lastBit = bit;
                        count = 1;
                    }
                }
            }
            if( count > 4 ) {
                addPoints( count - 2 );
            }
        }
        
        // Count pixels vertically
        for( int u = 0, n = qrStream.getSideLength(); u < n; u++ ) {
            for( int v = 0; v < n; v++ ) {
                if( v == 0 ) {
                    lastBit = qrStream.getBitAt( u, v );
                    bit = lastBit;
                    count = 1;
                } else {
                    bit = qrStream.getBitAt( u, v );
                    if( bit == lastBit ) {
                        count++;
                    } else {
                        if( count > 4 ) {
                            addPoints( count - 2 );
                        }
                        lastBit = bit;
                        count = 1;
                    }
                }
            }
            if( count > 4 ) {
                addPoints( count - 2 );
            }
        }
    }
    
}