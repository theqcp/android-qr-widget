package org.qcp.maths.fields.binary;

import org.qcp.maths.fields.GaloisField;

public class BinaryField extends GaloisField {
    
    /**
     * Field for use with Quick Response codes
     */
    public static final BinaryField QR = new BinaryField( 8, 0x011d );
    /**
     * Field for use with Data Matrix codes
     */
    public static final BinaryField DM = new BinaryField( 8, 0x012d );
    
    /**
     * Creates a binary finite field of specified width using the given
     * polynomial.
     * 
     * @param width
     *            Field width, as 2^width
     * @param primitive
     *            Primitive irreducible polynomial whose coefficients are
     *            represented by the bits of an int, where the least-significant
     *            bit represents the constant coefficient
     */
    public BinaryField( int width, int primitive ) {
        super( Double.valueOf( Math.pow( 2, width ) ).intValue(), primitive );
    }
    
    @Override
    protected void generate() {
        for( int x = 1, i = 0; i < getFieldWidth(); i++ ) {
            getExpTable()[i] = x;
            x <<= 1;
            if( x >= getFieldWidth() ) {
                x ^= getPrimitivePolynomial();
            }
        }
        for( int i = 0; i < getFieldWidth(); i++ ) {
            getLogTable()[getExpTable()[i]] = i;
        }
    }
    
}
