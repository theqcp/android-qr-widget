package org.qcp.qr.parts;

public enum QrMode implements QrBitStream {
    
    Numeric( false, false, false, true ), AlphaNumeric( false, false, true, false ), //
    Binary( false, true, false, false ), Japanese( true, false, false, false ), //
    
    Append( false, false, true, true ), Extended( false, true, true, true ), //
    Fnc1InFirst( false, true, false, true ), Fnc1InSecond( true, false, false, true ), //
    
    EndOfMessage( false, false, false, false );
    
    public final static int STREAM_LENGTH = 4;
    
    private final boolean[] stream;
    
    private QrMode( boolean... values ) {
        this.stream = values;
    }
    
    @Override
    public boolean[] getStream() {
        return this.stream.clone();
    }
    
    @Override
    public int getStreamLength() {
        return this.stream.length;
    }
    
}
