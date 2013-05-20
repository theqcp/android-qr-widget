package org.qcp.qr.parts;

import lombok.Data;

@Data
public abstract class QrAbstractPenaltyFunction implements QrPenaltyFunction {
    
    private int points = 0;
    private String name;
    
    protected void addPoints( int points ) {
        this.points += points;
    }
    
    @Override
    public int calculate( Qr2dBitStream qrStream ) {
        setPoints( 0 );
        run( qrStream );
        return getPoints();
    }
    
    protected abstract void run( Qr2dBitStream qrStream );
    
}
