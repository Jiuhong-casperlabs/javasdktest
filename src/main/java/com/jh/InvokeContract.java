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
        File pkfile = new File("/home/jh/keys/test1/public_key.pem");
        File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);


        // contract_hash whose entrypoint to be invoked
        String contracthash = "823a59e984060c33d4fde6adb80a0017d585c37d56f7c0350b57c8e7cd3b0080";

        // Set deploy.
        final Deploy deploy = casperSdk.makeDeploy(new DeployParams(
            operatorKeyPair.getPublic(),
            chainName,
            1,
            Instant.now().toEpochMilli(),
            DeployParams.DEFAULT_TTL,
            null),
        new StoredContractByHash(
            new ContractHash(contracthash),   //contracthash
                "hello_world",    // entrypoint
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
