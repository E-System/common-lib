package com.es.lib.common.security;

public interface Hash {

    String ALGORITHM_MD5 = "MD5";
    String ALGORITHM_SHA256 = "SHA256";

    String get(String value);

    default boolean valid(String value, String hash) {
        if (hash == null || value == null) {
            return false;
        }
        return get(value).equals(hash);
    }

    static Hash bcrypt() {
        return bcrypt(12);
    }

    static Hash bcrypt(int level) {
        return new BCryptHash(level);
    }

    static Hash md5() {
        return hash(ALGORITHM_MD5);
    }

    static Hash hash(String algorithm) {
        return new BasicHash(algorithm);
    }

    /**
     * Create SHA256 hmac instance
     *
     * @param secret secret key
     * @return SHA256 hmac instance
     */
    static Hash hmacSha256(String secret) {
        return hmac(ALGORITHM_SHA256, secret);
    }

    static Hash hmac(String algorithm, String secret) {
        return new HmacHash(algorithm, secret);
    }
}
