package com.eslibs.common.binary;

import com.eslibs.common.security.Hex;

import java.util.Base64;

public record ByteEncoder(byte[] bytes) {

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

    public String string() {
        return bytes == null ? null : new String(bytes);
    }
}