package org.qcp.qr.parts;

public abstract class QrAbstractBitStream implements QrBitStream {
    
    private final boolean[] stream;
    private final int length;
    
    private int index = 0;
    
    public QrAbstractBitStream( int length ) {
        this( new boolean[length] );
    }
    
    public QrAbstractBitStream( boolean[] stream ) {
        this.length = stream.length;
        this.stream = stream.clone();
    }
    
    public boolean[] getStream() {
        return stream.clone();
    }
    
    public int getStreamLength() {
        return length;
    }
    
    public boolean get( int index ) {
        return stream[index];
    }
    
    protected void set( int index, boolean value ) {
        stream[index] = value;
    }
    
    protected void setIndex( int index ) {
        this.index = index;
    }
    
    protected int getIndex() {
        return this.index;
    }
    
    protected int getFreeBits() {
        return length - index;
    }
    
    protected void write( boolean... bits ) {
        if( getFreeBits() >= bits.length ) {
            for( boolean bit: bits ) {
                stream[index++] = bit;
            }
        } else {
            throw new IndexOutOfBoundsException( String.format( "Tried to write %d bits; only %d bits free", bits.length, getFreeBits() ) );
        }
    }
    
}
