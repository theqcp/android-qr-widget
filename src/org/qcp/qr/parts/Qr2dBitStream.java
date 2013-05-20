package org.qcp.qr.parts;

import lombok.Getter;

public abstract class Qr2dBitStream extends QrAbstractBitStream {
    
    @Getter
    private final int sideLength;
    
    public Qr2dBitStream( int length, int sideLength ) {
        super( length );
        
        this.sideLength = sideLength;
    }
    
    public Qr2dBitStream( boolean[] stream, int sideLength ) {
        super( stream );
        
        this.sideLength = sideLength;
    }
    
    public boolean getBitAt( int x, int y ) {
        return get( i( x, y ) );
    }
    
    protected void setBitAt( int x, int y, boolean value ) {
        set( i( x, y ), value );
    }
    
    protected void toggleBitAt( int x, int y ) {
        set( i( x, y ), !get( i( x, y ) ) );
    }
    
    protected void writeAt( int x, int y, boolean... values ) {
        setIndex( i( x, y ) );
        write( values );
    }
    
    protected int i( int x, int y ) {
        return (y * sideLength) + (x % sideLength);
    }
    
}
