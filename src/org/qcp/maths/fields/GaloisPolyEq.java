package org.qcp.maths.fields;

import org.qcp.maths.polynomials.PolynomialEquation;
import org.qcp.maths.polynomials.PolynomialQuotientAndRemainder;

public class GaloisPolyEq<GF extends GaloisField> implements PolynomialEquation<GaloisPolyEq<GF>> {
    
    public static <GF extends GaloisField> GaloisPolyEq<GF> buildMonomial( GF field, int degree, int coefficient ) {
        if( degree < 0 ) {
            throw new IllegalArgumentException();
        }
        if( coefficient == 0 ) {
            return buildMonomialZero( field );
        }
        int[] coefficients = new int[degree + 1];
        coefficients[0] = coefficient;
        return new GaloisPolyEq<GF>( field, coefficients );
    }
    
    public static <GF extends GaloisField> GaloisPolyEq<GF> buildMonomialZero( GF field ) {
        return new GaloisPolyEq<GF>( field, 0 );
    }
    
    private final int[] coefficients;
    private final GF field;
    
    public GaloisPolyEq( GF field, int... coefficients ) {
        if( coefficients == null || coefficients.length == 0 ) {
            throw new IllegalArgumentException();
        }
        
        this.field = field;
        
        int coefficientsLength = coefficients.length;
        
        if( coefficientsLength > 1 && coefficients[0] == 0 ) {
            // Leading term must be non-zero for anything except the constant
            // polynomial "0"
            int firstNonZero = 1;
            
            while( firstNonZero < coefficientsLength && coefficients[firstNonZero] == 0 ) {
                firstNonZero++;
            }
            
            if( firstNonZero == coefficientsLength ) {
                this.coefficients = buildMonomialZero( field ).coefficients;
            } else {
                this.coefficients = new int[coefficientsLength - firstNonZero];
                System.arraycopy( coefficients, firstNonZero, this.coefficients, 0, this.coefficients.length );
            }
        } else {
            this.coefficients = coefficients;
        }
    }
    
    @Override
    public int getDegree() {
        return coefficients.length - 1;
    }
    
    @Override
    public boolean isZero() {
        return coefficients[0] == 0;
    }
    
    @Override
    public int[] getCoefficients() {
        return coefficients;
    }
    
    @Override
    public int getCoefficient( int degree ) {
        return coefficients[coefficients.length - 1 - degree];
    }
    
    @Override
    public int evaluateAt( int a ) {
        if( a == 0 ) {
            // Just return the x^0 coefficient
            return getCoefficient( 0 );
        }
        
        int size = coefficients.length;
        if( a == 1 ) {
            // Just the sum of the coefficients
            int result = 0;
            for( int i = 0; i < size; i++ ) {
                result = GaloisField.addOrSubtract( result, coefficients[i] );
            }
            return result;
        }
        
        int result = coefficients[0];
        for( int i = 1; i < size; i++ ) {
            result = GaloisField.addOrSubtract( field.multiply( a, result ), coefficients[i] );
        }
        
        return result;
    }
    
    @Override
    public GaloisPolyEq<GF> add( GaloisPolyEq<GF> other ) {
        if( !field.equals( other.field ) ) {
            throw new IllegalArgumentException( "Equations must have the same field" );
        }
        
        if( isZero() ) {
            return other;
        }
        if( other.isZero() ) {
            return this;
        }
        
        int[] smallerCoefficients = this.coefficients;
        int[] largerCoefficients = other.coefficients;
        
        if( smallerCoefficients.length > largerCoefficients.length ) {
            int[] temp = smallerCoefficients;
            smallerCoefficients = largerCoefficients;
            largerCoefficients = temp;
        }
        
        int[] sumDiff = new int[largerCoefficients.length];
        int lengthDiff = largerCoefficients.length - smallerCoefficients.length;
        
        // Copy high-order terms only found in higher-degree polynomial's
        // coefficients
        System.arraycopy( largerCoefficients, 0, sumDiff, 0, lengthDiff );
        
        for( int i = lengthDiff; i < largerCoefficients.length; i++ ) {
            sumDiff[i] = GaloisField.addOrSubtract( smallerCoefficients[i - lengthDiff], largerCoefficients[i] );
        }
        
        return new GaloisPolyEq<GF>( field, sumDiff );
    }
    
    @Override
    public GaloisPolyEq<GF> subtract( GaloisPolyEq<GF> other ) {
        return add( other );
    }
    
    @Override
    public GaloisPolyEq<GF> multiply( GaloisPolyEq<GF> other ) {
        if( !field.equals( other.field ) ) {
            throw new IllegalArgumentException( "Equations must have the same field" );
        }
        
        if( isZero() || other.isZero() ) {
            return buildMonomialZero( field );
        }
        
        int[] aCoefficients = this.coefficients;
        int aLength = aCoefficients.length;
        int[] bCoefficients = other.coefficients;
        int bLength = bCoefficients.length;
        int[] product = new int[aLength + bLength - 1];
        
        for( int i = 0; i < aLength; i++ ) {
            int aCoeff = aCoefficients[i];
            for( int j = 0; j < bLength; j++ ) {
                product[i + j] = GaloisField.addOrSubtract( product[i + j], field.multiply( aCoeff, bCoefficients[j] ) );
            }
        }
        
        return new GaloisPolyEq<GF>( field, product );
    }
    
    @Override
    public GaloisPolyEq<GF> multiply( int scalar ) {
        if( scalar == 0 ) {
            return buildMonomialZero( field );
        }
        if( scalar == 1 ) {
            return this;
        }
        
        int size = coefficients.length;
        int[] product = new int[size];
        
        for( int i = 0; i < size; i++ ) {
            product[i] = field.multiply( coefficients[i], scalar );
        }
        
        return new GaloisPolyEq<GF>( field, product );
    }
    
    @Override
    public GaloisPolyEq<GF> multiply( int degree, int coefficient ) {
        if( degree < 0 ) {
            throw new IllegalArgumentException();
        }
        if( coefficient == 0 ) {
            return buildMonomialZero( field );
        }
        
        int size = coefficients.length;
        int[] product = new int[size + degree];
        
        for( int i = 0; i < size; i++ ) {
            product[i] = field.multiply( coefficients[i], coefficient );
        }
        
        return new GaloisPolyEq<GF>( field, product );
    }
    
    @Override
    public PolynomialQuotientAndRemainder<GaloisPolyEq<GF>> divide( GaloisPolyEq<GF> other ) {
        if( !field.equals( other.field ) ) {
            throw new IllegalArgumentException( "Equations must have the same field" );
        }
        
        if( other.isZero() ) {
            throw new IllegalArgumentException( "Divide by 0" );
        }
        
        GaloisPolyEq<GF> quotient = buildMonomialZero( field );
        GaloisPolyEq<GF> remainder = this;
        
        int denominatorLeadingTerm = other.getCoefficient( other.getDegree() );
        int inverseDenominatorLeadingTerm = field.inverse( denominatorLeadingTerm );
        
        while( remainder.getDegree() >= other.getDegree() && !remainder.isZero() ) {
            int degreeDifference = remainder.getDegree() - other.getDegree();
            int scale = field.multiply( remainder.getCoefficient( remainder.getDegree() ), inverseDenominatorLeadingTerm );
            GaloisPolyEq<GF> term = other.multiply( degreeDifference, scale );
            GaloisPolyEq<GF> iterationQuotient = buildMonomial( field, degreeDifference, scale );
            quotient = quotient.add( iterationQuotient );
            remainder = remainder.add( term );
        }
        
        return new PolynomialQuotientAndRemainder<GaloisPolyEq<GF>>( quotient, remainder );
    }
    
    @Override
    public GaloisPolyEq<GF> divide( int scalar ) {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
    
    @Override
    public PolynomialQuotientAndRemainder<GaloisPolyEq<GF>> divide( int degree, int coefficient ) {
        throw new UnsupportedOperationException( "Not implemented yet" );
    }
    
    public String toString() {
        StringBuffer result = new StringBuffer( 8 * getDegree() );
        for( int degree = getDegree(); degree >= 0; degree-- ) {
            int coefficient = getCoefficient( degree );
            if( coefficient != 0 ) {
                if( coefficient < 0 ) {
                    result.append( " - " );
                    coefficient = -coefficient;
                } else {
                    if( result.length() > 0 ) {
                        result.append( " + " );
                    }
                }
                if( degree == 0 || coefficient != 1 ) {
                    int alphaPower = field.log( coefficient );
                    if( alphaPower == 0 ) {
                        result.append( '1' );
                    } else if( alphaPower == 1 ) {
                        result.append( 'a' );
                    } else {
                        result.append( "a^" );
                        result.append( alphaPower );
                    }
                }
                if( degree != 0 ) {
                    if( degree == 1 ) {
                        result.append( 'x' );
                    } else {
                        result.append( "x^" );
                        result.append( degree );
                    }
                }
            }
        }
        return result.toString();
    }
    
}
