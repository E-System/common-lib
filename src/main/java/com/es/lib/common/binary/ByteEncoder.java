package com.es.lib.common.binary;

import com.es.lib.common.security.Hex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Base64;

@Getter
@ToString
@RequiredArgsConstructor
public class ByteEncoder {

    private final byte[] bytes;

    public String urlEncode() {
        return encode(Base64.getUrlEncoder());
    }

    public String mimeEncode() {
        return encode(Base64.getMimeEncoder());
    }

    public String encode() {
        return encode(Base64.getEncoder());
    }

    public String encode(Base64.Encoder encoder) {
        if (bytes == null || encoder == null) {
            return null;
        }
        return encoder.encodeToString(bytes);
    }

    public String hexEncode() {
        return hexEncode(false);
    }

    public String hexEncode(boolean upperCase) {
        String result = Hex.get(bytes);
        if (result == null || !upperCase) {
            return result;
        }
        return result.toUpperCase();
    }
}