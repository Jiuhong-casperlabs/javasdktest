package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.*;

import java.security.KeyPair;
import java.time.Instant;

import java.io.File;

public class InvokeContract {
    public static void main(String[] args) {
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);
        final SigningService sss = new SigningService();

        // chain name
        final String chainName = "mynetwork";
        // payment
        final Number payment = 3e9;


        // Get operator keypair.
        File pkfile = new File("/Users/jh/keys/test1/public_key.pem");
        File skfile = new File("/Users/jh/keys/test1/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);


        // contract_hash whose entrypoint to be invoked
        String contracthash = "f841c6074845bedfa7c29d1650790f8fd4cf8436b1c24bc5d6d1fa0d777a6d58";

        // Set deploy.
        final Deploy deploy = casperSdk.makeDeploy(new DeployParams(
            operatorKeyPair.getPublic(),
            chainName,
            1,
            Instant.now().toEpochMilli(),
            DeployParams.DEFAULT_TTL,
            null),
        new StoredContractByHash(
            new ContractHash(contracthash),
                "hello_world",
                new DeployNamedArgBuilder()
                .build()
        ),
        casperSdk.standardPayment(payment));

        // Approve deploy.
        casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}
