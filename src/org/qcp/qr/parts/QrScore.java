package org.qcp.qr.parts;

import java.util.HashSet;

import org.qcp.qr.parts.pf.QrPenaltyFunction1;
import org.qcp.qr.parts.pf.QrPenaltyFunction2;
import org.qcp.qr.parts.pf.QrPenaltyFunction3;
import org.qcp.qr.parts.pf.QrPenaltyFunction4;

public final class QrScore {
    
    private static final HashSet<QrPenaltyFunction> PENALTY_FUNCTIONS = new HashSet<QrPenaltyFunction>();
    
    public static int result( Qr2dBitStream qrStream ) {
        int score = 0;
        
        for( QrPenaltyFunction function: PENALTY_FUNCTIONS ) {
            score += function.calculate( qrStream );
        }
        
        return score;
    }
    
    static {
        PENALTY_FUNCTIONS.add( QrPenaltyFunction1.INSTANCE );
        PENALTY_FUNCTIONS.add( QrPenaltyFunction2.INSTANCE );
        PENALTY_FUNCTIONS.add( QrPenaltyFunction3.INSTANCE );
        PENALTY_FUNCTIONS.add( QrPenaltyFunction4.INSTANCE );
    }
    
}
