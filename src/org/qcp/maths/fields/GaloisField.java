package org.qcp.maths.fields;

public abstract class GaloisField implements NumberField {
    
    private final int primitivePolynomial;
    private final int fieldWidth;
    
    private final int[] expTable, logTable;
    
    public GaloisField( int width, int primitive ) {
        this.fieldWidth = width;
        this.primitivePolynomial = primitive;
        
        expTable = new int[fieldWidth];
        logTable = new int[fieldWidth];
        
        generate();
    }
    
    /**
     * Populate field.
     */
    protected abstract void generate();
    
    public static int addOrSubtract( int a, int b ) {
        return a ^ b;
    }
    
    @Override
    public int exp( int a ) {
        return expTable[a];
    }
    
    @Override
    public int log( int a ) {
        if( a == 0 ) {
            throw new IllegalArgumentException();
        }
        return logTable[a];
    }
    
    @Override
    public int inverse( int a ) {
        if( a == 0 ) {
            throw new ArithmeticException();
        }
        return expTable[255 - logTable[a]];
    }
    
    @Override
    public int add( int a, int b ) {
        return addOrSubtract( a, b );
    }
    
    @Override
    public int multiply( int a, int b ) {
        if( a == 0 || b == 0 ) {
            return 0;
        }
        if( a == 1 ) {
            return b;
        }
        if( b == 1 ) {
            return a;
        }
        return expTable[(logTable[a] + logTable[b]) % 255];
    }
    
    public int getPrimitivePolynomial() {
        return primitivePolynomial;
    }
    
    public int getFieldWidth() {
        return fieldWidth;
    }
    
    public int[] getExpTable() {
        return expTable;
    }
    
    public int[] getLogTable() {
        return logTable;
    }
    
}
