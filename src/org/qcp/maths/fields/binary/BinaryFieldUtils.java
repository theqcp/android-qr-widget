package org.qcp.maths.fields.binary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinaryFieldUtils {
    
    private static final HashMap<BinaryField, List<BinaryPolyEq>> cachedGenerators = new HashMap<BinaryField, List<BinaryPolyEq>>();
    
    public static BinaryPolyEq buildGenerator( BinaryField field, int degree ) {
        List<BinaryPolyEq> cache = cachedGenerators.get( field );
        if( cache == null ) {
            cache = new ArrayList<BinaryPolyEq>();
            cache.add( new BinaryPolyEq( field, new int[] { 1 } ) );
        }
        
        if( degree >= cache.size() ) {
            BinaryPolyEq lastGenerator = cache.get( cache.size() - 1 );
            for( int d = cache.size(); d <= degree; d++ ) {
                BinaryPolyEq nextGenerator = lastGenerator.multiply( new BinaryPolyEq( field, new int[] { 1, field.exp( d - 1 ) } ) );
                cache.add( nextGenerator );
                lastGenerator = nextGenerator;
            }
        }
        
        return cache.get( degree );
    }
    
}
