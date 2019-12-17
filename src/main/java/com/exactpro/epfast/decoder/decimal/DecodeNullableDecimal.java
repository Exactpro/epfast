package com.exactpro.epfast.decoder.decimal;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeNullableInt32;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;

public final class DecodeNullableDecimal extends DecodeDecimal {

    private DecodeNullableInt32 exponentDecoder = new DecodeNullableInt32();

    private Integer exponent;

    private boolean nullValue;

    public void decode(ByteBuf buf) {
        reset();
        exponentDecoder.decode(buf);
        if (exponentDecoder.isReady()) {
            exponentReady = true;
            try {
                exponent = exponentDecoder.getValue();
            } catch (OverflowException ex) {
                exponentOverflow = true;
            }
            if (exponent != null && buf.isReadable()) {
                mantissaDecoder.decode(buf);
                startedMantissa = true;
                if (mantissaDecoder.isReady()) {
                    ready = true;
                    try {
                        mantissa = mantissaDecoder.getValue();
                    } catch (OverflowException ex) {
                        mantissaOverflow = true;
                    }
                }
            } else if (exponent == null) {
                nullValue = true;
                ready = true;
            }
        }
    }

    public void continueDecode(ByteBuf buf) {
        if (exponentReady && startedMantissa) {
            mantissaDecoder.continueDecode(buf);
            if (mantissaDecoder.isReady()) {
                ready = true;
                try {
                    mantissa = mantissaDecoder.getValue();
                } catch (OverflowException ex) {
                    mantissaOverflow = true;
                }
            }
        } else if (exponentReady) {
            startedMantissa = true;
            mantissaDecoder.decode(buf);
            if (mantissaDecoder.isReady()) {
                ready = true;
                try {
                    mantissa = mantissaDecoder.getValue();
                } catch (OverflowException ex) {
                    mantissaOverflow = true;
                }
            }
        } else {
            exponentDecoder.continueDecode(buf);
            if (exponentDecoder.isReady()) {
                exponentReady = true;
                try {
                    exponent = exponentDecoder.getValue();
                } catch (OverflowException ex) {
                    exponentOverflow = true;
                }
                if (exponent != null && buf.isReadable()) {
                    mantissaDecoder.decode(buf);
                    startedMantissa = true;
                    if (mantissaDecoder.isReady()) {
                        ready = true;
                        try {
                            mantissa = mantissaDecoder.getValue();
                        } catch (OverflowException ex) {
                            mantissaOverflow = true;
                        }
                    }
                } else if (exponent == null) {
                    nullValue = true;
                    ready = true;
                }
            }
        }
    }

    public BigDecimal getValue() throws OverflowException {
        if (nullValue) {
            return null;
        } else if (exponent >= -63 && exponent <= 63) {
            return new BigDecimal(mantissa).movePointRight(exponent);
        } else if (exponentOverflow) {
            throw new OverflowException("exponent value range is int32");
        } else if (mantissaOverflow) {
            throw new OverflowException("mantissa value range is int64");
        } else {
            throw new OverflowException("exponent value allowed range is -63 ... 63");
        }
    }

    @Override
    public boolean isOverlong() {
        return exponentDecoder.isOverlong() || mantissaDecoder.isOverlong();
    }
}
