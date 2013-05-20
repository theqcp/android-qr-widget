package org.qcp.qr.parts.pf;

import org.qcp.qr.parts.Qr2dBitStream;
import org.qcp.qr.parts.QrAbstractPenaltyFunction;

/**
 * Gives a penalty of 40 for sequences that look like positioning boxes.
 */
public class QrPenaltyFunction3 extends QrAbstractPenaltyFunction {

    private static final String PATTERN = "1011101";
    private static final int PENALTY = 40;

    public static final QrPenaltyFunction3 INSTANCE = new QrPenaltyFunction3("Penalty rule 3");

    private QrPenaltyFunction3(String name) {
        setName(name);
    }

    private void test(String sequence) {
        String pattern = new String(sequence);

        while( pattern.contains(PATTERN + "0000") ) {
            pattern = pattern.replaceFirst(PATTERN + "0000", "22222220000");
            addPoints(PENALTY);
        }
        while( pattern.contains("0000" + PATTERN) ) {
            pattern = pattern.replaceFirst("0000" + PATTERN, "00002222222");
            addPoints(PENALTY);
        }
    }

    @Override
    protected void run(Qr2dBitStream qrStream) {

        StringBuilder b;

        // Count pixels horizontally
        for( int v = 0, n = qrStream.getSideLength(); v < n; v++ ) {
            b = new StringBuilder(qrStream.getSideLength());
            for( int u = 0; u < n; u++ ) {
                b.append(qrStream.getBitAt(u, v) ? "1" : "0");
            }
            test(b.toString());
        }

        // Count pixels vertically
        for( int u = 0, n = qrStream.getSideLength(); u < n; u++ ) {
            b = new StringBuilder(qrStream.getSideLength());
            for( int v = 0; v < n; v++ ) {
                b.append(qrStream.getBitAt(u, v) ? "1" : "0");
            }
            test(b.toString());
        }
    }
}