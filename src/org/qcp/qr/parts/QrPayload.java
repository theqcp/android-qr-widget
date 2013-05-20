package org.qcp.qr.parts;

import lombok.Getter;

@Getter
public class QrPayload extends QrAbstractBitStream {
    
    public static QrPayload forMessage( QrMode mode, QrEcc eccLevel, int version, String message ) {
        return new QrPayload( mode, eccLevel, version ).fill( message );
    }
    
    public static QrPayload forMessage( QrMode mode, QrEcc eccLevel, int version, boolean[] stream ) {
        return new QrPayload( mode, eccLevel, version ).fill( stream );
    }
    
    private final QrEcc eccLevel;
    private final QrMode mode;
    private final int version;
    
    public QrPayload( QrMode mode, QrEcc eccLevel, int version ) {
        super( QrUtils.dataBitsForLevelAndVersion( eccLevel, version ) );
        
        this.mode = mode;
        this.eccLevel = eccLevel;
        this.version = version;
    }
    
    public QrPayload clone() {
        return new QrPayload( mode, eccLevel, version ).fill( getStream() );
    }
    
    public QrPayload fill( boolean[] stream ) {
        setIndex( 0 );
        write( stream );
        return this;
    }
    
    public QrPayload fill( String data ) {
        int dataLength = data.getBytes().length;
        // int freeBits;
        
        setIndex( 0 );
        
        write( mode.getStream() );
        write( QrUtils.makeLength( mode, version, dataLength ) );
        write( QrUtils.translateToAlphaNumeric( data ) );
        
        if( getFreeBits() > 0 ) {
            for( int i = 0, n = Math.min( 4, getFreeBits() ); i < n; i++ ) {
                write( false );
            }
            for( int i = 0, n = getFreeBits() % 8; i < n; i++ ) {
                write( false );
            }
        }
        
        if( getFreeBits() > 0 ) {
            int fillerByte = 0;
            do {
                write( QrUtils.SYMBOL_FILLER_BITS[fillerByte % 2] );
                fillerByte++;
            } while( getFreeBits() > 0 );
        }
        
        return this;
    }
    
}
