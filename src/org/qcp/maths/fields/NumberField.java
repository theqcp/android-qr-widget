package org.qcp.maths.fields;

/**
 * Functions to be available for any number field.
 * 
 * @author Billy Hamlin
 */
public interface NumberField {
    
    public int exp( int a );
    
    public int log( int a );
    
    public int inverse( int a );
    
    public int add( int a, int b );
    
    public int multiply( int a, int b );
    
}
