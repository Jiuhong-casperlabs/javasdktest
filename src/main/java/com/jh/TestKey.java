package com.jh;

import com.casper.sdk.service.signing.SigningService;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.security.KeyPair;
import java.io.OutputStream;
import java.io.FileOutputStream;

import com.casper.sdk.types.*;

public class TestKey {
    public static void main(String args[]) throws IOException {
        final SigningService signingService = new SigningService();
        final KeyPair keyPair = signingService.generateKeyPair(Algorithm.ED25519);

        System.out.println("===account address=========");
        final CLPublicKey publicKey = signingService.toClPublicKey(keyPair.getPublic());
        System.out.println(publicKey.toAccountHex());
        System.out.println("============");

        // create PRIVATE KEY file
        final ByteArrayOutputStream privateOut = new ByteArrayOutputStream();
        signingService.writeKey(privateOut, keyPair.getPrivate());
        System.out.println(privateOut);
        System.out.println("============");

        // create PUBLIC KEY file
        final ByteArrayOutputStream publicOut = new ByteArrayOutputStream();
        signingService.writeKey(publicOut, keyPair.getPublic());
        System.out.println(publicOut);
        System.out.println("============");

        // write private key to file
        OutputStream spoutputStream = new FileOutputStream("private.pem");
        privateOut.writeTo(spoutputStream);

        // write public key to file
        OutputStream pkoutputStream = new FileOutputStream("public.pem");
        publicOut.writeTo(pkoutputStream);
    }
}