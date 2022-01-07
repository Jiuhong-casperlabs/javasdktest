package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.ModuleBytes;
import com.casper.sdk.types.Transfer;

// import com.casper.sdk.KeyPairStreams;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Instant;
import java.io.File;


public class TestTransfer {
    public static void main(String args[]) {
        System.out.println("Hello Java");
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);

        final SigningService sss = new SigningService();
        File pkfile = new File("E:\\learning\\java\\test\\keys\\test1\\test1_public_key.pem");
        File skfile = new File("E:\\learning\\java\\test\\keys\\test1\\test1_secret_key.pem"); 
        final KeyPair kp = sss.loadKeyPair(pkfile,skfile);

        final PublicKey publicKey = kp.getPublic();


        final Transfer transfer = casperSdk.newTransfer(
                10,
                publicKey,
                34
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

        casperSdk.putDeploy(deploy);
    }
}