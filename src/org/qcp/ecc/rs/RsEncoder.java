package org.qcp.ecc.rs;

import org.qcp.maths.fields.binary.BinaryField;
import org.qcp.maths.fields.binary.BinaryFieldUtils;
import org.qcp.maths.fields.binary.BinaryPolyEq;
import org.qcp.utils.Encoder;

public class RsEncoder implements Encoder<int[], int[]> {
    
    public static int[] encode( int[] input, int indexCorrection ) {
        return encode( BinaryField.QR, input, indexCorrection );
    }
    
    public static int[] encode( BinaryField field, int[] input, int indexCorrection ) {
        BinaryPolyEq generator = BinaryFieldUtils.buildGenerator( field, indexCorrection );
        BinaryPolyEq info = (BinaryPolyEq) new BinaryPolyEq( field, input ).multiply( indexCorrection, 1 );
        BinaryPolyEq remainder = (BinaryPolyEq) info.divide( generator ).getRemainder();
        int[] coefficients = remainder.getCoefficients();
        
        int[] output = new int[input.length + indexCorrection];
        System.arraycopy( input, 0, output, 0, input.length );
        System.arraycopy( coefficients, 0, output, input.length, indexCorrection );
        return output;
    }
    
    public final BinaryField field;
    public int indexCorrection;
    
    public RsEncoder( BinaryField field, int indexCorrection ) {
        this.field = field;
        this.indexCorrection = indexCorrection;
    }
    
    @Override
    public int[] encode( int[] input ) {
        return encode( field, input, indexCorrection );
    }
    
}
