package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.ModuleBytes;
import com.casper.sdk.types.Transfer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Instant;
import java.io.File;

import com.casper.sdk.types.*;

public class TestTransfer {
    public static void main(String args[]) {
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);

        final SigningService sss = new SigningService();

        // 
        File pkfile = new File("/home/jh/keys/test1/public_key.pem");
        
        // File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        // "/home/jh/keys/test1/secret_key.pem"
        File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        final KeyPair kp = sss.loadKeyPair(pkfile, skfile);
        
        File pkfile1 = new File("/home/jh/keys/test77/public_key.pem");
        
        // File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        // "/home/jh/keys/test1/secret_key.pem"
        File skfile1 = new File("/home/jh/keys/test77/secret_key.pem");
        final KeyPair kp1 = sss.loadKeyPair(pkfile1,skfile1);

        final PublicKey publicKey = kp.getPublic();
        final PublicKey publicKey1 = kp1.getPublic();


        final Transfer transfer = casperSdk.newTransfer(
            new BigInteger("2500000000"),
            publicKey1,
                1
        );

        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));
        
        Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                    publicKey,
                        "casper-test",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null
                ),
                transfer,
                payment
        );

       deploy =  casperSdk.signDeploy(deploy, kp);
        try {
            final String json = casperSdk.deployToJson(deploy);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Digest digest = casperSdk.putDeploy(deploy);
        final Deploy networkDeploy = casperSdk.getDeploy(digest);

        System.out.println(networkDeploy);
    }
}