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

package com.eslibs.common.security;


import com.eslibs.common.binary.Bytes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
@RequiredArgsConstructor
public final class RSASigner {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String algorithm;
    private final Charset charset;

    public RSASigner(PrivateKey privateKey, PublicKey publicKey) {
        this(privateKey, publicKey, "SHA1withRSA");
    }

    public RSASigner(PrivateKey privateKey, PublicKey publicKey, String algorithm) {
        this(privateKey, publicKey, algorithm, Bytes.DEFAULT_ENCODING);
    }

    /**
     * Create sign
     *
     * @param value   Data for create signature
     * @param reverse Reverse signed bytes
     * @return Sign in Base64
     * @throws SignatureException Create signature exception
     */
    public String sign(String value, boolean reverse) throws SignatureException {
        try {
            Signature sign = Signature.getInstance(algorithm);
            sign.initSign(privateKey);
            sign.update(value.getBytes(charset));
            byte[] res = sign.sign();
            if (reverse) {
                ArrayUtils.reverse(res);
            }
            return new String(Base64.getEncoder().encode(res), charset);
        } catch (Exception ex) {
            throw new SignatureException(ex);
        }
    }

    /**
     * Проверяет подпись
     *
     * @param value     Data for verify signature
     * @param signature Signature in Base64
     * @return true if signature valid
     * @throws SignatureException Signature verify exception
     */
    public boolean verify(String value, String signature) throws SignatureException {
        try {
            Signature sign = Signature.getInstance(algorithm);
            sign.initVerify(publicKey);
            sign.update(value.getBytes(charset));
            return sign.verify(Base64.getDecoder().decode(signature.getBytes(charset)));
        } catch (Exception ex) {
            throw new SignatureException(ex);
        }
    }
}
