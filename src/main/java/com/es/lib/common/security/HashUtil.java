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

package com.es.lib.common.security;

import com.es.lib.common.Constant;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.CRC32;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@Slf4j
public final class HashUtil {

    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA256 = "SHA256";
    private static final String HEXES = "0123456789abcdef";

    private HashUtil() { }

    public static String hash(byte[] value, String algorithm) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(value);
            return getHex(digest.digest());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String hash(String value, String algorithm) {
        if (value == null) {
            return null;
        }
        try {
            return hash(value.getBytes(Constant.DEFAULT_ENCODING), algorithm);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String md5(String value) {
        return hash(value, ALGORITHM_MD5);
    }

    public static String md5(byte[] value) {
        return hash(value, ALGORITHM_MD5);
    }

    public static boolean isCorrect(final String text, final String hash) {
        return md5(text).equals(hash);
    }

    public static String hmac(String algorithm, String value, String secret) {
        try {
            String alg = "Hmac" + algorithm;
            Mac hmac = Mac.getInstance(alg);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(Constant.DEFAULT_ENCODING), alg);
            hmac.init(secret_key);

            return Base64.getEncoder().encodeToString(hmac.doFinal(value.getBytes(Constant.DEFAULT_ENCODING)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Calculate SHA256 hmac
     *
     * @param value  message text
     * @param secret secret key
     * @return Base64 of calculated hash
     */
    public static String hmacSha256(String value, String secret) {
        return hmac(ALGORITHM_SHA256, value, secret);
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

    public static long crc32(String string) throws UnsupportedEncodingException {
        return crc32(string.getBytes(Constant.DEFAULT_ENCODING));
    }

    public static long crc32(byte[] bytes) {
        CRC32 crc = new CRC32();
        crc.update(bytes);
        return crc.getValue();
    }

    public static int crc16(final byte[] buffer) {
        int crc = 0xFFFF;
        for (byte aBuffer : buffer) {
            crc = ((crc >>> 8) | (crc << 8)) & 0xFFFF;
            crc ^= (aBuffer & 0xFF);
            crc ^= ((crc & 0xFF) >> 4);
            crc ^= (crc << 12) & 0xFFFF;
            crc ^= ((crc & 0xFF) << 5) & 0xFFFF;
        }
        crc &= 0xFFFF;
        return crc;
    }

    public static int crc16ccitt(byte[] data) { return crc16ccitt(data, 0, 0); }

    public static int crc16ccitt(byte[] data, int skipIndex, int skipLen) {
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
        crc &= 0xFFFF;
        return crc;
    }
}