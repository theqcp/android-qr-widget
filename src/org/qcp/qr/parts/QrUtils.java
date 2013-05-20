package org.qcp.qr.parts;

import java.util.HashMap;

import org.qcp.ecc.rs.RsEncoder;
import org.qcp.maths.fields.binary.BinaryField;
import org.qcp.qr.ex.UnsupportedQrVersionException;

import android.util.SparseIntArray;

public final class QrUtils {
    
    public static final SparseIntArray SYMBOL_LOOKUP = new SparseIntArray( 45 );
    public static final HashMap<QrEcc, boolean[][]> MASK_LOOKUP = new HashMap<QrEcc, boolean[][]>();
    public static final boolean[] SYMBOL_FILLER_BITS_ONE = new boolean[] { true, true, true, false, true, true, false, false };
    public static final boolean[] SYMBOL_FILLER_BITS_TWO = new boolean[] { false, false, false, true, false, false, false, true };
    public static final boolean[][] SYMBOL_FILLER_BITS = new boolean[][] { SYMBOL_FILLER_BITS_ONE, SYMBOL_FILLER_BITS_TWO };
    public static final int SYMBOL_BITS = 11, SYMBOL_TAIL_BITS = 6;
    
    static {
        SYMBOL_LOOKUP.put( (int) '0', 0 );
        SYMBOL_LOOKUP.put( (int) '1', 1 );
        SYMBOL_LOOKUP.put( (int) '2', 2 );
        SYMBOL_LOOKUP.put( (int) '3', 3 );
        SYMBOL_LOOKUP.put( (int) '4', 4 );
        SYMBOL_LOOKUP.put( (int) '5', 5 );
        SYMBOL_LOOKUP.put( (int) '6', 6 );
        SYMBOL_LOOKUP.put( (int) '7', 7 );
        SYMBOL_LOOKUP.put( (int) '8', 8 );
        SYMBOL_LOOKUP.put( (int) '9', 9 );
        SYMBOL_LOOKUP.put( (int) 'A', 10 );
        SYMBOL_LOOKUP.put( (int) 'B', 11 );
        SYMBOL_LOOKUP.put( (int) 'C', 12 );
        SYMBOL_LOOKUP.put( (int) 'D', 13 );
        SYMBOL_LOOKUP.put( (int) 'E', 14 );
        SYMBOL_LOOKUP.put( (int) 'F', 15 );
        SYMBOL_LOOKUP.put( (int) 'G', 16 );
        SYMBOL_LOOKUP.put( (int) 'H', 17 );
        SYMBOL_LOOKUP.put( (int) 'I', 18 );
        SYMBOL_LOOKUP.put( (int) 'J', 19 );
        SYMBOL_LOOKUP.put( (int) 'K', 20 );
        SYMBOL_LOOKUP.put( (int) 'L', 21 );
        SYMBOL_LOOKUP.put( (int) 'M', 22 );
        SYMBOL_LOOKUP.put( (int) 'N', 23 );
        SYMBOL_LOOKUP.put( (int) 'O', 24 );
        SYMBOL_LOOKUP.put( (int) 'P', 25 );
        SYMBOL_LOOKUP.put( (int) 'Q', 26 );
        SYMBOL_LOOKUP.put( (int) 'R', 27 );
        SYMBOL_LOOKUP.put( (int) 'S', 28 );
        SYMBOL_LOOKUP.put( (int) 'T', 29 );
        SYMBOL_LOOKUP.put( (int) 'U', 30 );
        SYMBOL_LOOKUP.put( (int) 'V', 31 );
        SYMBOL_LOOKUP.put( (int) 'W', 32 );
        SYMBOL_LOOKUP.put( (int) 'X', 33 );
        SYMBOL_LOOKUP.put( (int) 'Y', 34 );
        SYMBOL_LOOKUP.put( (int) 'Z', 35 );
        SYMBOL_LOOKUP.put( (int) ' ', 36 );
        SYMBOL_LOOKUP.put( (int) '$', 37 );
        SYMBOL_LOOKUP.put( (int) '%', 38 );
        SYMBOL_LOOKUP.put( (int) '*', 39 );
        SYMBOL_LOOKUP.put( (int) '+', 40 );
        SYMBOL_LOOKUP.put( (int) '-', 41 );
        SYMBOL_LOOKUP.put( (int) '.', 42 );
        SYMBOL_LOOKUP.put( (int) '/', 43 );
        SYMBOL_LOOKUP.put( (int) ':', 44 );
        
        MASK_LOOKUP.put( QrEcc.Quartile, new boolean[][] { { false, true, true, false, true, false, true, false, true, false, true, true, true, true, true, },
                { false, true, true, false, false, false, false, false, true, true, false, true, false, false, false, },
                { false, true, true, true, true, true, true, false, false, true, true, false, false, false, true, },
                { false, true, true, true, false, true, false, false, false, false, false, false, true, true, false, },
                { false, true, false, false, true, false, false, true, false, true, true, false, true, false, false, },
                { false, true, false, false, false, false, true, true, false, false, false, false, false, true, true, },
                { false, true, false, true, true, true, false, true, true, false, true, true, false, true, false, },
                { false, true, false, true, false, true, true, true, true, true, false, true, true, false, true, }, } );
    }
    
    public static QrStream bestMask( QrStream qrStream ) {
        HashMap<QrMask, QrStream> streams = new HashMap<QrMask, QrStream>();
        HashMap<QrMask, Integer> scores = new HashMap<QrMask, Integer>();
        
        QrStream workingCopy;
        for( QrMask mask: QrMask.values() ) {
            workingCopy = decorate( mask, qrStream.clone() );
            streams.put( mask, workingCopy );
            // System.out.println( "---------------" );
            // System.out.println( mask );
            scores.put( mask, QrScore.result( workingCopy ) );
            // System.out.println( scores.get( mask ) );
        }
        
        QrMask lowest = QrMask.Mask0;
        for( QrMask mask: QrMask.values() ) {
            if( scores.get( lowest ) > scores.get( mask ) ) {
                lowest = mask;
            }
        }
        
        // System.out.println( lowest );
        return streams.get( lowest );
    }
    
    public static QrStream decorate( QrMask mask, QrStream qrStream ) {
        int sideLength = qrStream.getSideLength();
        boolean[] maskStream = mask.getMaskBitStream( qrStream.getEccLevel() );
        
        // currently only version 1 is implemented
        if( qrStream.getVersion() == 1 ) {
            for( int v = 0, n = sideLength; v < n; v++ ) {
                for( int u = 0; u < n; u++ ) {
                    if( mask.flipLoc( u, v ) ) {
                        qrStream.toggleBitAt( u, v );
                    }
                }
            }
            
            { // Position code at (0,0)
                qrStream.writeAt( 0, 0, true, true, true, true, true, true, true, false );
                qrStream.writeAt( 0, 1, true, false, false, false, false, false, true, false );
                qrStream.writeAt( 0, 2, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, 3, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, 4, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, 5, true, false, false, false, false, false, true, false );
                qrStream.writeAt( 0, 6, true, true, true, true, true, true, true, false );
                qrStream.writeAt( 0, 7, false, false, false, false, false, false, false, false );
            }
            
            { // Position code at (xMax, 0)
                qrStream.writeAt( sideLength - 8, 0, false, true, true, true, true, true, true, true );
                qrStream.writeAt( sideLength - 8, 1, false, true, false, false, false, false, false, true );
                qrStream.writeAt( sideLength - 8, 2, false, true, false, true, true, true, false, true );
                qrStream.writeAt( sideLength - 8, 3, false, true, false, true, true, true, false, true );
                qrStream.writeAt( sideLength - 8, 4, false, true, false, true, true, true, false, true );
                qrStream.writeAt( sideLength - 8, 5, false, true, false, false, false, false, false, true );
                qrStream.writeAt( sideLength - 8, 6, false, true, true, true, true, true, true, true );
                qrStream.writeAt( sideLength - 8, 7, false, false, false, false, false, false, false, false );
            }
            
            { // Position code at (0, yMax)
                qrStream.writeAt( 0, sideLength - 8, false, false, false, false, false, false, false, false, true );
                qrStream.writeAt( 0, sideLength - 7, true, true, true, true, true, true, true, false );
                qrStream.writeAt( 0, sideLength - 6, true, false, false, false, false, false, true, false );
                qrStream.writeAt( 0, sideLength - 5, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, sideLength - 4, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, sideLength - 3, true, false, true, true, true, false, true, false );
                qrStream.writeAt( 0, sideLength - 2, true, false, false, false, false, false, true, false );
                qrStream.writeAt( 0, sideLength - 1, true, true, true, true, true, true, true, false );
            }
            
            { // Timing pattern on X, Y=6
                for( int u = 8, v = 6; u < sideLength - 8; u++ ) {
                    qrStream.setBitAt( u, v, u % 2 == 0 );
                }
            }
            
            { // Timing pattern on X=6, Y
                for( int u = 6, v = 8; v < sideLength - 8; v++ ) {
                    qrStream.setBitAt( u, v, v % 2 == 0 );
                }
            }
            
            { // Type information at X, Y=8
                for( int u = 0, v = 8; u < 6; u++ ) {
                    qrStream.setBitAt( u, v, maskStream[u] );
                }
                qrStream.setBitAt( 7, 8, maskStream[6] );
                for( int u = sideLength - 8, v = 8, n = u; u < sideLength; u++ ) {
                    qrStream.setBitAt( u, v, maskStream[u - n + 7] );
                }
            }
            
            { // Type information for X=8, Y
                for( int u = 8, v = 0; v < 7; v++ ) {
                    qrStream.setBitAt( u, sideLength - 1 - v, maskStream[v] );
                }
                qrStream.setBitAt( 8, 8, maskStream[7] );
                qrStream.setBitAt( 8, 7, maskStream[8] );
                for( int u = 8, v = 9, n = maskStream.length; v < n; v++ ) {
                    qrStream.setBitAt( u, n - v - 1, maskStream[v] );
                }
            }
        }
        
        return qrStream;
    }
    
    public static int dataBitsForLevelAndVersion( QrEcc eccLevel, int version ) {
        if( (eccLevel == QrEcc.Quartile) && (version == 1) ) {
            return 104;
        }
        
        return 1;
    }
    
    @SuppressWarnings( "incomplete-switch" )
    public static int dataLengthBitCount( QrMode mode, int version ) {
        if( mode != null ) {
            if( (version >= 1) && (version <= 9) ) {
                switch( mode ) {
                    case Numeric:
                        return 10;
                    case AlphaNumeric:
                        return 9;
                    case Binary:
                        return 8;
                    case Japanese:
                        return 8;
                }
            } else if( (version >= 10) && (version <= 26) ) {
                switch( mode ) {
                    case Numeric:
                        return 12;
                    case AlphaNumeric:
                        return 11;
                    case Binary:
                        return 16;
                    case Japanese:
                        return 10;
                }
            } else if( (version >= 27) && (version <= 40) ) {
                switch( mode ) {
                    case Numeric:
                        return 14;
                    case AlphaNumeric:
                        return 13;
                    case Binary:
                        return 16;
                    case Japanese:
                        return 12;
                }
            }
            
            throw new UnsupportedQrVersionException( version );
        }
        
        throw new NullPointerException( "Mode must not be null" );
    }
    
    public static int firstNonZero( int[] array ) {
        int i = array.length - 1;
        
        while( (array[i] == 0) && (i > 0) ) {
            i--;
        }
        
        return i;
    }
    
    public static int getBitCountForChars( int count ) {
        return ((int) Math.floor( count / 2 )) * SYMBOL_BITS + (count % 2) * SYMBOL_TAIL_BITS;
    }
    
    public static int getBitsPerSide( int version ) {
        return (version * 4) + 17;
    }
    
    public static boolean[] getMaskPattern( QrEcc eccLevel, int maskPattern ) {
        return MASK_LOOKUP.get( eccLevel )[maskPattern];
    }
    
    public static boolean[] getStreamWithEcc( boolean[] data, int correctionCount ) {
        int[] dataArray = toIntArray( data );
        return toBitStream( RsEncoder.encode( BinaryField.QR, dataArray, dataArray.length ) );
    }
    
    public static boolean[] getStreamWithEcc( QrPayload data ) {
        return getStreamWithEcc( data.getStream(), 13 );
    }
    
    public static int getTotalBitsForVersion( int version ) {
        return (int) Math.pow( getBitsPerSide( version ), 2 );
    }
    
    public static boolean[] makeLength( QrMode mode, int version, int dataLength ) {
        return setBits( new boolean[dataLengthBitCount( mode, version )], 0, dataLengthBitCount( mode, version ), dataLength );
    }
    
    public static QrStream mask( QrStream inStream ) {
        QrStream outStream = inStream.clone();
        
        // outStream.getPayload().fill( inStream.getPayload().getStream() );
        
        return outStream;
    }
    
    public static boolean[] setBits( boolean[] data, int start, int count, int value ) {
        for( int i = 0; i < count; i++ ) {
            data[count - i - 1 + start] = (value & ((int) Math.pow( 2, i ))) != 0;
        }
        
        return data;
    }
    
    public static boolean[] toBitStream( int[] array ) {
        boolean[] stream = new boolean[array.length * 8];
        
        for( int i = 0, n = array.length; i < n; i++ ) {
            setBits( stream, i * 8, 8, array[i] );
        }
        
        return stream;
    }
    
    public static int[] toIntArray( boolean[] stream ) {
        int count = (int) Math.ceil( stream.length / 8 );
        int[] array = new int[count];
        
        for( int i = 0, n = array.length; i < n; i++ ) {
            array[i] = 0;
        }
        
        int u, v;
        u = v = 0;
        
        for( int i = 0, n = stream.length; i < n; i++ ) {
            if( stream[i] ) {
                u = i / 8;
                v = i % 8;
                
                array[u] |= 128 >> v;
            }
        }
        
        return array;
    }
    
    public static boolean[] translateToAlphaNumeric( String data ) {
        byte[] charStream = data.toUpperCase().getBytes();
        int remaining = charStream.length;
        boolean[] bits = new boolean[getBitCountForChars( remaining )];
        
        int index = 0;
        int i, j, v;
        while( remaining > 0 ) {
            if( remaining > 1 ) {
                i = SYMBOL_LOOKUP.get( (int) charStream[index + 0] );
                j = SYMBOL_LOOKUP.get( (int) charStream[index + 1] );
                
                v = 45 * i + j;
                
                setBits( bits, (index / 2) * SYMBOL_BITS, SYMBOL_BITS, v );
                
                index += 2;
                remaining -= 2;
            } else {
                v = SYMBOL_LOOKUP.get( (int) charStream[index] );
                
                setBits( bits, (index / 2) * SYMBOL_BITS, SYMBOL_TAIL_BITS, v );
                
                index += 1;
                remaining -= 1;
            }
        }
        
        return bits;
    }
    
}
