package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.*;

import java.security.KeyPair;
import java.time.Instant;
import java.io.InputStream;
import java.io.File;

public class Installcontract {
    public static void main(String[] args) {
        // final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);
        final CasperSdk casperSdk = new CasperSdk("http://35.169.205.205", 7777);
        
        final SigningService sss = new SigningService();

        // contract code github: https://github.com/Jiuhong-casperlabs/disable-enable-contract
        // contract wasm path => 
        final InputStream contractwasmIn = HowToUtils.getWasmIn("wasm/contract.wasm");

        // chain name
        // final String chainName = "mynetwork";
        final String chainName = "casper-test";
        // payment
        final Number payment = 30e9;

        // Get operator keypair.
        File pkfile = new File("/home/jh/keys/test12/public_key.pem");
        File skfile = new File("/home/jh/keys/test12/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);

        // Set deploy.
        final Deploy deploy = casperSdk.makeDeploy(new DeployParams(
            operatorKeyPair.getPublic(),
            chainName,
            1,
            Instant.now().toEpochMilli(),
            DeployParams.DEFAULT_TTL,
            null),
            new ModuleBytes(HowToUtils.readWasm(contractwasmIn),
                        new DeployNamedArgBuilder()
                                .build()
                ),
            casperSdk.standardPayment(payment)
    );

        // Approve deploy.
        casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}
