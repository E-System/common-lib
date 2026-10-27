package com.es.lib.common

import spock.lang.Specification

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SignatureException

class RSASignerSpec extends Specification {

    def "Sign and verify message with default and custom algorithms"() {
        given:
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(2048)
        KeyPair keyPair = kpg.generateKeyPair()

        RSASigner signerDefault = new RSASigner(keyPair.private, keyPair.public)
        RSASigner signerSha256 = new RSASigner(keyPair.private, keyPair.public, "SHA256withRSA")
        RSASigner signerCustom = new RSASigner(keyPair.private, keyPair.public, "SHA256withRSA", "UTF-8")

        String message = "Message to sign"

        when:
        String sig1 = signerDefault.sign(message)
        String sig2 = signerSha256.sign(message)
        String sig3 = signerCustom.sign(message)
        String sigRev = signerCustom.signReverse(message)

        then:
        signerDefault.verify(message, sig1)
        !signerDefault.verify("Tampered message", sig1)
        signerSha256.verify(message, sig2)
        signerCustom.verify(message, sig3)
        sigRev != null
        sigRev.length() > 0
    }

    def "Invalid signature or parameters throw SignatureException"() {
        given:
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        KeyPair keyPair = kpg.generateKeyPair()

        RSASigner invalidSigner = new RSASigner(keyPair.private, keyPair.public, "INVALID_ALGO")

        when:
        invalidSigner.sign("test")

        then:
        thrown(SignatureException)

        when:
        invalidSigner.signReverse("test")

        then:
        thrown(SignatureException)

        when:
        invalidSigner.verify("test", "sig")

        then:
        thrown(SignatureException)
    }
}
