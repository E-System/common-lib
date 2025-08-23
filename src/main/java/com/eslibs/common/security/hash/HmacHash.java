package com.eslibs.common.security.hash;

import com.eslibs.common.binary.ByteEncoder;
import com.eslibs.common.binary.Bytes;
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
    public String of(String value) {
        try {
            return of(Bytes.of(value));
        } catch (Exception _) {
            return null;
        }
    }

    @Override
    public String of(byte[] value) {
        try {
            String alg = "Hmac" + algorithm;
            Mac mac = Mac.getInstance(alg);
            SecretKeySpec secretKeySpec = new SecretKeySpec(Bytes.of(secret), alg);
            mac.init(secretKeySpec);
            ByteEncoder encoder = new ByteEncoder(mac.doFinal(value));
            return hexEncode ? encoder.hex() : encoder.base64();
        } catch (Exception _) {
            return null;
        }
    }
}
