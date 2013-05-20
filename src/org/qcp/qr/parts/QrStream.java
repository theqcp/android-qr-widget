package org.qcp.qr.parts;

public class QrStream extends Qr2dBitStream {
    
    public static final int[] VERSION_1_FILL_ORDER = new int[] {
    /*    */0, 0, 0, 0, 0, 0, 0, 0, 0, 137, 136, 135, 134, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 139, 138, 133, 132, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 141, 140, 131, 130, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 143, 142, 129, 128, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 145, 144, 127, 126, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 147, 146, 125, 124, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 149, 148, 123, 122, 0, 0, 0, 0, 0, 0, 0, 0, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 151, 150, 121, 120, 0, 0, 0, 0, 0, 0, 0, 0, //
            201, 200, 199, 198, 185, 184, 0, 183, 182, 153, 152, 119, 118, 73, 72, 71, 70, 25, 24, 23, 22, //
            203, 202, 197, 196, 187, 186, 0, 181, 180, 155, 154, 117, 116, 75, 74, 69, 68, 27, 26, 21, 20, //
            205, 204, 195, 194, 189, 188, 0, 179, 178, 157, 156, 115, 114, 77, 76, 67, 66, 29, 28, 19, 18, //
            207, 206, 193, 192, 191, 190, 0, 177, 176, 159, 158, 113, 112, 79, 78, 65, 64, 31, 30, 17, 16, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 161, 160, 111, 110, 81, 80, 63, 62, 33, 32, 15, 14, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 163, 162, 109, 108, 83, 82, 61, 60, 35, 34, 13, 12, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 165, 164, 107, 106, 85, 84, 59, 58, 37, 36, 11, 10, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 167, 166, 105, 104, 87, 86, 57, 56, 39, 38, 9, 8, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 169, 168, 103, 102, 89, 88, 55, 54, 41, 40, 7, 6, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 171, 170, 101, 100, 91, 90, 53, 52, 43, 42, 5, 4, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 173, 172, 99, 98, 93, 92, 51, 50, 45, 44, 3, 2, //
            0, 0, 0, 0, 0, 0, 0, 0, 0, 175, 174, 97, 96, 95, 94, 49, 48, 47, 46, 1, 0 //
    };
    
    protected final QrPayload payload;
    
    public QrStream( QrMode mode, QrEcc eccLevel, int version ) {
        super( QrUtils.getTotalBitsForVersion( version ), QrUtils.getBitsPerSide( version ) );
        
        this.payload = new QrPayload( mode, eccLevel, version );
    }
    
    public QrStream( QrPayload data ) {
        super( QrUtils.getTotalBitsForVersion( data.getVersion() ), QrUtils.getBitsPerSide( data.getVersion() ) );
        
        this.payload = data;
        
        setIndex( 0 );
        boolean[] dataStream = QrUtils.getStreamWithEcc( data );
        
        for( int i = 0, n = getStreamLength(); i < n; i++ ) {
            set( i, dataStream[VERSION_1_FILL_ORDER[i]] );
        }
    }
    
    public QrStream( QrStream stream ) {
        super( QrUtils.getTotalBitsForVersion( stream.getVersion() ), QrUtils.getBitsPerSide( stream.getVersion() ) );
        
        this.payload = stream.payload.clone();
        
        setIndex( 0 );
        write( stream.getStream() );
    }
    
    public QrMode getMode() {
        return payload.getMode();
    }
    
    public QrEcc getEccLevel() {
        return payload.getEccLevel();
    }
    
    public int getVersion() {
        return payload.getVersion();
    }
    
    protected QrStream clone() {
        return new QrStream( this );
    }
    
}
