package org.qcp.qr.parts;

import java.util.HashMap;

public enum QrMask {
    
    Mask0( new boolean[][] { { true, true, true, false, true, true, true, true, true, false, false, false, true, false, false },
            { true, false, true, false, true, false, false, false, false, false, true, false, false, true, false },
            { false, true, true, false, true, false, true, false, true, false, true, true, true, true, true },
            { false, false, true, false, true, true, false, true, false, false, false, true, false, false, true } } ), //
    Mask1( new boolean[][] { { true, true, true, false, false, true, false, true, true, true, true, false, false, true, true },
            { true, false, true, false, false, false, true, false, false, true, false, false, true, false, true },
            { false, true, true, false, false, false, false, false, true, true, false, true, false, false, false },
            { false, false, true, false, false, true, true, true, false, true, true, true, true, true, false } } ), //
    Mask2( new boolean[][] { { true, true, true, true, true, false, true, true, false, true, false, true, false, true, false },
            { true, false, true, true, true, true, false, false, true, true, true, true, true, false, false },
            { false, true, true, true, true, true, true, false, false, true, true, false, false, false, true },
            { false, false, true, true, true, false, false, true, true, true, false, false, true, true, true } } ), //
    Mask3( new boolean[][] { { true, true, true, true, false, false, false, true, false, false, true, true, true, false, true },
            { true, false, true, true, false, true, true, false, true, false, false, true, false, true, true },
            { false, true, true, true, false, true, false, false, false, false, false, false, true, true, false },
            { false, false, true, true, false, false, true, true, true, false, true, false, false, false, false } } ), //
    Mask4( new boolean[][] { { true, true, false, false, true, true, false, false, false, true, false, true, true, true, true },
            { true, false, false, false, true, false, true, true, true, true, true, true, false, false, true },
            { false, true, false, false, true, false, false, true, false, true, true, false, true, false, false },
            { false, false, false, false, true, true, true, false, true, true, false, false, false, true, false } } ), //
    Mask5( new boolean[][] { { true, true, false, false, false, true, true, false, false, false, true, true, false, false, false },
            { true, false, false, false, false, false, false, true, true, false, false, true, true, true, false },
            { false, true, false, false, false, false, true, true, false, false, false, false, false, true, true },
            { false, false, false, false, false, true, false, false, true, false, true, false, true, false, true } } ), //
    Mask6( new boolean[][] { { true, true, false, true, true, false, false, false, true, false, false, false, false, false, true },
            { true, false, false, true, true, true, true, true, false, false, true, false, true, true, true },
            { false, true, false, true, true, true, false, true, true, false, true, true, false, true, false },
            { false, false, false, true, true, false, true, false, false, false, false, true, true, false, false } } ), //
    Mask7( new boolean[][] { { true, true, false, true, false, false, true, false, true, true, true, false, true, true, false },
            { true, false, false, true, false, true, false, true, false, true, false, false, false, false, false },
            { false, true, false, true, false, true, true, true, true, true, false, true, true, false, true },
            { false, false, false, true, false, false, false, false, false, true, true, true, false, true, true } } );
    
    private final HashMap<QrEcc, boolean[]> maskBitStreams = new HashMap<QrEcc, boolean[]>( QrEcc.values().length );
    
    private QrMask( boolean[][] masks ) {
        QrEcc[] eccLevels = new QrEcc[] { QrEcc.Low, QrEcc.Medium, QrEcc.Quartile, QrEcc.High };
        for( int i = 0, n = eccLevels.length; i < n; i++ ) {
            maskBitStreams.put( eccLevels[i], masks[i] );
        }
    }
    
    public boolean[] getMaskBitStream( QrEcc eccLevel ) {
        return maskBitStreams.get( eccLevel ).clone();
    }
    
    public boolean flipLoc( int u, int v ) {
        switch( this ) {
            case Mask0:
                return 0 == ((u + v) % 2);
            case Mask1:
                return 0 == (v % 2);
            case Mask2:
                return 0 == (u % 3);
            case Mask3:
                return 0 == ((u + v) % 3);
            case Mask4:
                return 0 == (((u / 3) + (v / 2)) % 2);
            case Mask5:
                return 0 == (((u * v) % 2) + ((u * v) % 3));
            case Mask6:
                return 0 == ((((u * v) % 2) + ((u * v) % 3)) % 2);
            case Mask7:
                return 0 == ((((u + v) % 2) + ((u * v) % 3)) % 2);
            default:
                return false;
        }
    }
    
}
