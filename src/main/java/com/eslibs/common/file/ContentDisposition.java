package com.eslibs.common.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentDisposition {

    private static final BitSet PRINTABLE = new BitSet(256);

    static {
        for (int i = 33; i <= 126; i++) {
            PRINTABLE.set(i);
        }
        PRINTABLE.set(61, false); // =
        PRINTABLE.set(63, false); // ?
        PRINTABLE.set(95, false); // _
    }

    public static String encode(boolean attachment, String fileName, Charset charset) {
        StringBuilder sb = new StringBuilder(attachment ? "attachment" : "inline");
        if (charset == null || StandardCharsets.US_ASCII.equals(charset)) {
            sb.append("; filename=\"");
            sb.append(encodeQuotedPairs(fileName)).append('\"');
        } else {
            sb.append("; filename=\"");
            sb.append(encodeQuotedPrintableFilename(fileName, charset)).append('\"');
            sb.append("; filename*=");
            sb.append(encodeRfc5987Filename(fileName, charset));
        }
        return sb.toString();
    }

    private static String encodeQuotedPrintableFilename(String filename, Charset charset) {
        byte[] source = filename.getBytes(charset);
        StringBuilder sb = new StringBuilder(source.length << 1);
        sb.append("=?");
        sb.append(charset.name());
        sb.append("?Q?");
        for (byte b : source) {
            if (b == 32) { // RFC 2047, section 4.2, rule (2)
                sb.append('_');
            } else if (isPrintable(b)) {
                sb.append((char) b);
            } else {
                sb.append('=');
                char ch1 = hexDigit(b >> 4);
                char ch2 = hexDigit(b);
                sb.append(ch1);
                sb.append(ch2);
            }
        }
        sb.append("?=");
        return sb.toString();
    }

    private static boolean isPrintable(byte c) {
        int b = c;
        if (b < 0) {
            b = 256 + b;
        }
        return PRINTABLE.get(b);
    }

    private static String encodeQuotedPairs(String filename) {
        if (filename.indexOf('"') == -1 && filename.indexOf('\\') == -1) {
            return filename;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filename.length(); i++) {
            char c = filename.charAt(i);
            if (c == '"' || c == '\\') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static String encodeRfc5987Filename(String input, Charset charset) {
        byte[] source = input.getBytes(charset);
        StringBuilder sb = new StringBuilder(source.length << 1);
        sb.append(charset.name());
        sb.append("''");
        for (byte b : source) {
            if (isRFC5987AttrChar(b)) {
                sb.append((char) b);
            } else {
                sb.append('%');
                char hex1 = hexDigit(b >> 4);
                char hex2 = hexDigit(b);
                sb.append(hex1);
                sb.append(hex2);
            }
        }
        return sb.toString();
    }

    private static char hexDigit(int b) {
        return Character.toUpperCase(Character.forDigit(b & 0xF, 16));
    }

    private static boolean isRFC5987AttrChar(byte c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
               c == '!' || c == '#' || c == '$' || c == '&' || c == '+' || c == '-' ||
               c == '.' || c == '^' || c == '_' || c == '`' || c == '|' || c == '~';
    }
}
