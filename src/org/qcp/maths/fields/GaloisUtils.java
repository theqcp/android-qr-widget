package org.qcp.maths.fields;

import java.util.List;

import android.util.SparseArray;

public class GaloisUtils {
    
    private static final SparseArray<List<GaloisPolyEq<?>>> cachedGenerators = new SparseArray<List<GaloisPolyEq<?>>>();
    
    @SuppressWarnings( "unchecked" )
    public static <GF extends GaloisField> GaloisPolyEq<GF> buildGenerator( GF field, int degree ) {
        List<GaloisPolyEq<?>> cache = cachedGenerators.get( degree );
        
        if( degree >= cachedGenerators.size() ) {
            GaloisPolyEq<GF> lastGenerator = (GaloisPolyEq<GF>) cache.get( cachedGenerators.size() - 1 );
            for( int d = cachedGenerators.size(); d <= degree; d++ ) {
                GaloisPolyEq<GF> nextGenerator = lastGenerator.multiply( new GaloisPolyEq<GF>( field, new int[] { 1, field.exp( d - 1 ) } ) );
                ((List<GaloisPolyEq<? extends GaloisField>>) cachedGenerators).add( nextGenerator );
                lastGenerator = nextGenerator;
            }
        }
        
        return (GaloisPolyEq<GF>) cachedGenerators.get( degree );
    }
    
}
