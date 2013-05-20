package org.qcp.qr.ex;

public class UnsupportedQrVersionException extends RuntimeException {
    
    private static final long serialVersionUID = 4159220862512097943L;
    
    public UnsupportedQrVersionException( int version ) {
        super( String.format( "Version '%d' is not in the range [1, 40]", version ) );
    }
    
}
