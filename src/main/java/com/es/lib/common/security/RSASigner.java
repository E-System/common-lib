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
import org.apache.commons.lang3.ArrayUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 10.04.15
 */
public final class RSASigner {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String algoritm;
    private final String charset;

    public RSASigner(PrivateKey privateKey, PublicKey publicKey) {
        this(privateKey, publicKey, "SHA1withRSA", Constant.DEFAULT_ENCODING);
    }

    public RSASigner(PrivateKey privateKey, PublicKey publicKey, String algorithm) {
        this(privateKey, publicKey, algorithm, Constant.DEFAULT_ENCODING);
    }

    public RSASigner(PrivateKey privateKey, PublicKey publicKey, String algoritm, String charset) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.algoritm = algoritm;
        this.charset = charset;
    }

    /**
     * Подписывает строку
     *
     * @param message - Строка для подписи
     * @return подпись в Base64
     * @throws SignatureException - ошибка формирования подписи
     */
    public String sign(String message) throws SignatureException {
        try {
            Signature sign = Signature.getInstance(algoritm);
            sign.initSign(privateKey);
            sign.update(message.getBytes(charset));

            return new String(Base64.getEncoder().encode(sign.sign()), charset);
        } catch (Exception ex) {
            throw new SignatureException(ex);
        }
    }

    /**
     * Подписывает строку переставляя байты
     *
     * @param message - Строка для подписи
     * @return подпись в Base64
     * @throws SignatureException - ошибка формирования подписи
     */
    public String signReverse(String message) throws SignatureException {
        try {
            Signature sign = Signature.getInstance(algoritm);
            sign.initSign(privateKey);
            sign.update(message.getBytes(charset));
            byte[] res = sign.sign();
            ArrayUtils.reverse(res);
            return new String(Base64.getEncoder().encode(res), charset);
        } catch (Exception ex) {
            throw new SignatureException(ex);
        }
    }

    /**
     * Проверяет подпись
     *
     * @param message   строка для проверки
     * @param signature подись в Base64
     * @return true если подпись верна
     * @throws SignatureException - ошибка верификации подписи
     */
    public boolean verify(String message, String signature) throws SignatureException {
        try {
            Signature sign = Signature.getInstance(algoritm);
            sign.initVerify(publicKey);
            sign.update(message.getBytes(charset));
            return sign.verify(Base64.getDecoder().decode(signature.getBytes(charset)));
        } catch (Exception ex) {
            throw new SignatureException(ex);
        }
    }
}
