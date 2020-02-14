package com.es.lib.common;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLUtil {

    private static final String SSL_TYPE_DEFAULT = "SSL";

    public static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        return createSSLContext(SSL_TYPE_DEFAULT);
    }

    public static SSLContext createSSLContext(String sslType) throws NoSuchAlgorithmException, KeyManagementException {
        return createSSLContext(sslType, null, createTrustManager());
    }

    public static SSLContext createSSLContext(Path keyStorePath, String keyStoreType, String keyStorePassword, String keyPassword)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return createSSLContext(SSL_TYPE_DEFAULT, keyStorePath, keyStoreType, keyStorePassword, keyPassword);
    }

    public static SSLContext createSSLContext(String sslType, Path keyStorePath, String keyStoreType, String keyStorePassword, String keyPassword)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return createSSLContext(sslType, createKeyManager(keyStorePath, keyStoreType, keyStorePassword, keyPassword), createTrustManager());
    }

    public static SSLContext createSSLContext(String sslType, KeyManager[] keyManagers, TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(sslType);
        sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
        return sslContext;
    }

    private static TrustManager createTrustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) { }

            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        };
    }

    private static KeyManager[] createKeyManager(Path keyStorePath, String keyStoreType, String keyStorePassword, String keyPassword) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore clientStore = loadKeyStore(keyStorePath, keyStoreType, keyStorePassword);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, getPasswordSafe(keyPassword));
        return kmf.getKeyManagers();
    }

    private static KeyStore loadKeyStore(Path keyStorePath, String keyStoreType, String keyStorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore clientStore = KeyStore.getInstance(keyStoreType);
        try (InputStream storeStream = Files.newInputStream(keyStorePath)) {
            clientStore.load(storeStream, getPasswordSafe(keyStorePassword));
        }
        return clientStore;
    }

    private static char[] getPasswordSafe(String password) {
        return StringUtils.isNotBlank(password) ? password.toCharArray() : null;
    }
}
