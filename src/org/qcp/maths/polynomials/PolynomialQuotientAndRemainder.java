package org.qcp.maths.polynomials;

public class PolynomialQuotientAndRemainder<T extends PolynomialEquation<?>> {
    
    private final T quotient, remainder;
    
    public PolynomialQuotientAndRemainder( T quotient, T remainder ) {
        this.quotient = quotient;
        this.remainder = remainder;
    }
    
    public T getQuotient() {
        return quotient;
    }
    
    public T getRemainder() {
        return remainder;
    }
    
}
