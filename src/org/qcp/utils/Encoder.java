package org.qcp.utils;

public interface Encoder<IN, OUT extends IN> {
    
    public OUT encode( IN input );
    
}
