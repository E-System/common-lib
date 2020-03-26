package com.es.lib.common.security;

import com.es.lib.common.binary.ByteEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HmacHash implements StrHash {

    private final String algorithm;
    private final String secret;

    @Override
    public String get(String value) {
        try {
            String alg = "Hmac" + algorithm;
            Mac mac = Mac.getInstance(alg);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), alg);
            mac.init(secret_key);
            return new ByteEncoder(mac.doFinal(value.getBytes(StandardCharsets.UTF_8))).encode();
        } catch (Exception ignore) {
            return null;
        }
    }
}
