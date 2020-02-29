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

import lombok.extern.slf4j.Slf4j;

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

    public static Hash bcrypt() {
        return bcrypt(12);
    }

    public static Hash bcrypt(int level) {
        return new HashBCrypt(level);
    }

    public static Hash md5() {
        return hash(ALGORITHM_MD5);
    }

    public static Hash hash(String algorithm) {
        return new HashBasic(algorithm);
    }

    /**
     * Create SHA256 hmac instance
     *
     * @param secret secret key
     * @return SHA256 hmac instance
     */
    public static Hash hmacSha256(String secret) {
        return hmac(ALGORITHM_SHA256, secret);
    }

    public static Hash hmac(String algorithm, String secret) {
        return new HashHmac(algorithm, secret);
    }

    public static NumericHash crc32() {
        return new Crc32Hash();
    }

    public static NumericHash crc16() {
        return new Crc16Hash();
    }

    public static NumericHash crc16ccitt() {
        return crc16ccitt(0, 0);
    }

    public static NumericHash crc16ccitt(int skipIndex, int skipLen) {
        return new Crc16ccittHash(skipIndex, skipLen);
    }

    public static String toHex(final byte[] value) {
        if (value == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(0x2 * value.length);
        for (final byte b : value) {
            hex.append(HEXES.charAt((b & 240) >> 0x4)).append(HEXES.charAt(b & 15));
        }
        return hex.toString();
    }
}
