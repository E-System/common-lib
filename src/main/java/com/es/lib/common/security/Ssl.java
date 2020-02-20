package com.es.lib.common.security;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Ssl {

    private static final String SSL_TYPE_DEFAULT = "SSL";

    public static SSLContext context() throws NoSuchAlgorithmException, KeyManagementException {
        return context(SSL_TYPE_DEFAULT);
    }

    public static SSLContext context(String sslType) throws NoSuchAlgorithmException, KeyManagementException {
        return context(sslType, null, trustManager());
    }

    public static SSLContext context(com.es.lib.common.security.model.KeyStore keyStore)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(SSL_TYPE_DEFAULT, keyStore);
    }

    public static SSLContext context(String sslType, com.es.lib.common.security.model.KeyStore keyStore)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(sslType, keyManager(keyStore), trustManager());
    }

    public static SSLContext context(String sslType, KeyManager[] keyManagers, TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(sslType);
        sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
        return sslContext;
    }

    public static TrustManager trustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) { }

            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        };
    }

    public static HostnameVerifier allowAllHostVerifier() {
        return (s, sslSession) -> true;
    }

    private static KeyManager[] keyManager(com.es.lib.common.security.model.KeyStore keyStore) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        java.security.KeyStore clientStore = load(keyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, asArray(keyStore.getKeyPassword()));
        return kmf.getKeyManagers();
    }

    private static java.security.KeyStore load(com.es.lib.common.security.model.KeyStore keyStore) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        java.security.KeyStore clientStore = KeyStore.getInstance(keyStore.getType());
        try (InputStream storeStream = Files.newInputStream(keyStore.getPath())) {
            clientStore.load(storeStream, asArray(keyStore.getStorePassword()));
        }
        return clientStore;
    }

    private static char[] asArray(String password) {
        return StringUtils.isNotBlank(password) ? password.toCharArray() : null;
    }
}
