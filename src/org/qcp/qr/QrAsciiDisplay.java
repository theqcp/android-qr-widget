package org.qcp.qr;

import org.qcp.qr.parts.Qr2dBitStream;

public class QrAsciiDisplay {
    
    private static String rowOf( int count, String border, String item ) {
        return rowOf( count, border, item, border );
    }
    
    private static String rowOf( int count, String borderLeft, String item, String borderRight ) {
        StringBuilder b = new StringBuilder();
        
        b.append( borderLeft );
        for( int i = 0; i < count; i++ ) {
            b.append( item );
        }
        b.append( borderRight );
        
        return b.toString();
    }
    
    private static String s( int... charCode ) {
        return new String( charCode, 0, charCode.length );
    }
    
    public static String toString( Qr2dBitStream stream ) {
        StringBuilder b = new StringBuilder();
        int sideLength = stream.getSideLength();
        
        b.append( rowOf( sideLength + 6, s( 201 ), s( 205, 205 ), s( 187 ) ) ).append( '\n' );
        
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        
        for( int j = 0; j < sideLength; j++ ) {
            b.append( s( 186 ) ).append( "      " );
            for( int i = 0; i < sideLength; i++ ) {
                b.append( stream.getBitAt( i, j ) ? s( 219, 219 ) : "  " );
            }
            b.append( "      " ).append( s( 186 ) ).append( '\n' );
        }
        
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        b.append( rowOf( sideLength + 6, s( 186 ), "  " ) ).append( '\n' );
        
        b.append( rowOf( sideLength + 6, s( 200 ), s( 205, 205 ), s( 188 ) ) );
        
        return b.toString();
    }
    
}
