/*
 * Copyright 2016 E-System LLC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.es.lib.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class HashUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HashUtil.class);
    private static final String HEXES = "0123456789abcdef";

    private HashUtil() {
    }

    public static String hash(String text, String algorithm) {
        if (text == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(text.getBytes(Constant.DEFAULT_ENCODING));
            return getHex(digest.digest());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public static String md5(String text) {
        return hash(text, "MD5");
    }

    public static boolean isCorrect(final String text, final String hash) {
        return md5(text).equals(hash);
    }

    public static String getHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(0x2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 240) >> 0x4)).append(HEXES.charAt(b & 15));
        }
        return hex.toString();
    }

    public static int crc16(final byte[] buffer) {
        int crc = 0xFFFF;
        for (int j = 0; j < buffer.length; j++) {
            crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
            crc ^= (buffer[j] & 0xff);
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    public static int CRC16CCITT(byte[] data) { return CRC16CCITT(data, 0, 0); }

    public static int CRC16CCITT(byte[] data, int skipIndex, int skipLen) {
        int crc = 0xFFFF;          // init
        int polynom = 0x1021;   // 0001 0000 0010 0001
        for (int i = 0; i < data.length; i++) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            for (int j = 0; j < 8; j++) {
                boolean bit = ((data[i] >> (7 - j) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynom;
            }
        }
        crc &= 0xffff;
        return crc;
    }

    public static int XOR(byte[] data) { return XOR(data, 0, 0); }

    public static int XOR(byte[] data, int skipIndex, int skipLen) {
        return XOR(data, skipIndex, skipLen, data.length);
    }

    public static int XOR(byte[] data, int skipIndex, int skipLen, int lastIdx) {
        byte res = 0x00;
        for (int i = 0; i < lastIdx; i++) {
            if (i >= skipIndex && i < skipIndex + skipLen) {
                continue;
            }
            res ^= data[i];
        }
        return res;
    }
}
