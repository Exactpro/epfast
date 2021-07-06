/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public final class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x02000000_00000000L;

    private byte[] bytes = new byte[8];

    private boolean isUInt64Limit;

    private long value;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readerLimit = buf.writerIndex();
        if (bytesRead == 0) {
            int oneByte = getByte(buf, readerIndex++);
            value = 0;
            isUInt64Limit = false;
            accumulate(oneByte);
            if (!ready && (readerIndex < readerLimit)) {
                readerIndex = continuePositive(buf, readerIndex, readerLimit);
            }
        } else {
            readerIndex = continuePositive(buf, readerIndex, readerLimit);
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setResult(register);
            return FINISHED;
        } else {
            return MORE_DATA_NEEDED;
        }
    }

    private int continuePositive(ByteBuf buf, int readerIndex, int readerLimit) {
        do {
            int oneByte = getByte(buf, readerIndex++);
            if (bytesRead == 2) {
                checkOverlong(oneByte);
            }
            accumulate(oneByte);
        } while (!ready && (readerIndex < readerLimit));
        return readerIndex;
    }

    private void accumulate(int oneByte) {
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            isUInt64Limit = true;
        } else {
            overflow = true;
        }
    }

    private void setResult(UnionRegister register) {
        if (isUInt64Limit) {
            longToBytes(-1L, bytes);
        } else {
            longToBytes(value - 1, bytes);
        }
        register.uInt64Value = new BigInteger(1, bytes);
        register.isNull = value == 0;
        register.isOverlong = overlong;
        register.isOverflow = overflow;
        register.infoMessage = "UInt64 Overflow";
        reset();
    }

    private void checkOverlong(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }
}
