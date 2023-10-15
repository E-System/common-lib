package com.eslibs.common.security;

import com.eslibs.common.Constant;
import com.eslibs.common.binary.ByteEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class HmacHash implements StrHash {

    private final String algorithm;
    private final String secret;
    private final boolean hexEncode;

    @Override
    public String get(String value) {
        try {
            return get(Constant.bytes(value));
        } catch (Exception ignore) {
            return null;
        }
    }

    @Override
    public String get(byte[] value) {
        try {
            String alg = "Hmac" + algorithm;
            Mac mac = Mac.getInstance(alg);
            SecretKeySpec secretKeySpec = new SecretKeySpec(Constant.bytes(secret), alg);
            mac.init(secretKeySpec);
            ByteEncoder encoder = new ByteEncoder(mac.doFinal(value));
            return hexEncode ? encoder.hexEncode() : encoder.encode();
        } catch (Exception ignore) {
            return null;
        }
    }
}
