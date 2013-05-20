package org.qcp.qr;

import lombok.Getter;

import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrEcc;
import org.qcp.qr.parts.QrMode;
import org.qcp.qr.parts.QrPayload;
import org.qcp.qr.parts.QrStream;
import org.qcp.qr.parts.QrUtils;

@Getter
public class QrCode {
    
    public static QrCode forMessage( QrMode mode, QrEcc eccLevel, int version, String message ) {
        return new QrCode( new QrStream( QrPayload.forMessage( mode, eccLevel, version, message ) ) );
    }
    
    private final Qr2dBitStream stream;
    private final QrMode mode;
    private final QrEcc eccLevel;
    private final int version;
    
    public QrCode( QrStream qrStream ) {
        this.stream = QrUtils.bestMask( qrStream );
        
        this.mode = qrStream.getMode();
        this.eccLevel = qrStream.getEccLevel();
        this.version = qrStream.getVersion();
    }
    
    public Qr2dBitStream getStream() {
        return stream;
    }
    
}
