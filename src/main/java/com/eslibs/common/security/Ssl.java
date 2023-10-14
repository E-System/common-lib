package com.eslibs.common.security;

import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Ssl {

    public enum Mode {
        SSL,
        TLS
    }

    public static SSLContext context() throws NoSuchAlgorithmException, KeyManagementException {
        return context(true);
    }

    public static SSLContext context(boolean allTrust) throws NoSuchAlgorithmException, KeyManagementException {
        return context(Mode.SSL, allTrust);
    }

    public static SSLContext context(Mode sslType) throws NoSuchAlgorithmException, KeyManagementException {
        return context(sslType, true);
    }

    public static SSLContext context(Mode sslType, boolean allTrust) throws NoSuchAlgorithmException, KeyManagementException {
        return context(sslType, null, allTrust ? trustManager() : null);
    }

    public static SSLContext context(com.eslibs.common.security.model.KeyStore keyStore)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(keyStore, true);
    }

    public static SSLContext context(com.eslibs.common.security.model.KeyStore keyStore, boolean allTrust)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(Mode.SSL, keyStore, allTrust);
    }

    public static SSLContext context(Mode sslType, com.eslibs.common.security.model.KeyStore keyStore)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(sslType, keyStore, true);
    }

    public static SSLContext context(Mode sslType, com.eslibs.common.security.model.KeyStore keyStore, boolean allTrust)
        throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        return context(sslType, keyManager(keyStore), allTrust ? trustManager() : null);
    }

    public static SSLContext context(Mode sslType, KeyManager[] keyManagers, TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(sslType.name());
        sslContext.init(keyManagers, trustManager != null ? new TrustManager[]{trustManager} : null, new SecureRandom());
        return sslContext;
    }

    public static TrustManager trustManager() {
        return new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        };
    }

    public static HostnameVerifier allowAllHostVerifier() {
        return (s, sslSession) -> true;
    }

    private static KeyManager[] keyManager(com.eslibs.common.security.model.KeyStore keyStore) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        java.security.KeyStore clientStore = load(keyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, asArray(keyStore.getKeyPassword()));
        return kmf.getKeyManagers();
    }

    private static java.security.KeyStore load(com.eslibs.common.security.model.KeyStore keyStore) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
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
