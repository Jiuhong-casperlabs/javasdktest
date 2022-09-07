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
        final CasperSdk casperSdk = new CasperSdk("http://117.107.141.162", 7777);

        final SigningService sss = new SigningService();

        // source account keypair
        File pkfile = new File("/Users/jh/keys/chengdu/public_key.pem");
        File skfile = new File("/Users/jh/keys/chengdu/secret_key.pem");
        final KeyPair source = sss.loadKeyPair(pkfile, skfile);
        
        // Get target public key.
        final PublicKey targetpublicKey = casperSdk.createPublicKey("01f5f5677e7abb2c7cca5b3db730d0a87d94a7d7af19ed307ac552e77406e7929f");


        final PublicKey sourcepublicKey = source.getPublic();


        final Transfer transfer = casperSdk.newTransfer(
            new BigInteger("2500000000"),
            targetpublicKey,
                1
        );

        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));
        
        Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                    sourcepublicKey,
                        "chengdu",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null
                ),
                transfer,
                payment
        );

       deploy =  casperSdk.signDeploy(deploy, source);
        try {
            final String json = casperSdk.deployToJson(deploy);
            System.out.println(json);
            System.out.println("====json====");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}