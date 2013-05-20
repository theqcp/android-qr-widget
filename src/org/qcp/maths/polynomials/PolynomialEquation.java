package org.qcp.maths.polynomials;

public interface PolynomialEquation<TYPE extends PolynomialEquation<TYPE>> {
    
    public int getDegree();
    
    public boolean isZero();
    
    public int[] getCoefficients();
    
    public int getCoefficient( int degree );
    
    public int evaluateAt( int a );
    
    public TYPE add( TYPE equation );
    
    public TYPE subtract( TYPE equation );
    
    public TYPE multiply( TYPE equation );
    
    public TYPE multiply( int scalar );
    
    public TYPE multiply( int degree, int coefficient );
    
    public PolynomialQuotientAndRemainder<TYPE> divide( TYPE equation );
    
    public TYPE divide( int scalar );
    
    public PolynomialQuotientAndRemainder<TYPE> divide( int degree, int coefficient );
    
}
