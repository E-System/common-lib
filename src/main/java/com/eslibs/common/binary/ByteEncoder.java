package com.eslibs.common.binary;

import java.util.Base64;
import java.util.HexFormat;

public record ByteEncoder(byte[] bytes) {

    public String url() {
        return base64(Base64.getUrlEncoder());
    }

    public String mime() {
        return base64(Base64.getMimeEncoder());
    }

    public String base64() {
        return base64(Base64.getEncoder());
    }

    public String base64(Base64.Encoder encoder) {
        if (bytes == null || encoder == null) {
            return null;
        }
        return encoder.encodeToString(bytes);
    }

    public String hex() {
        return hex(false);
    }

    public String hex(boolean upperCase) {
        if (bytes == null) {
            return null;
        }
        HexFormat hexFormat = HexFormat.of();
        if (upperCase) {
            hexFormat = hexFormat.withUpperCase();
        }
        return hexFormat.formatHex(bytes);
    }

    public String string() {
        return bytes == null ? null : new String(bytes);
    }
}