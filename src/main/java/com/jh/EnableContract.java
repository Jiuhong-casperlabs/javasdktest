package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.*;

import java.security.KeyPair;
import java.time.Instant;
import java.io.InputStream;
import java.io.File;

public class EnableContract {
    public static void main(String[] args) {
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);
        final SigningService sss = new SigningService();

        // contract wasm path => 
        // original ~/casper-node/target/wasm32-unknown-unknown/release/enable_contract.wasm
        final InputStream contractwasmIn = HowToUtils.getWasmIn("wasm/enable_contract.wasm");

        // chain name
        final String chainName = "mynetwork";
        // payment
        final Number payment = 3e9;


        // Get admin keypair.
        File pkfile = new File("/home/jh/keys/test1/public_key.pem");
        File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);


        // args for contract -- contract_package_hash
        String contractackagehash = "5658b25984f5259aef86ca26de738d91525dfcb70103dcd17fe9397dbe8b4744";
        // args for contract -- contract_hash
        String contracthash = "f841c6074845bedfa7c29d1650790f8fd4cf8436b1c24bc5d6d1fa0d777a6d58";

        
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
                                .add("contract_package_hash", new CLValue(ByteUtils.decodeHex(contractackagehash), new CLByteArrayInfo(32), contractackagehash))
                                .add("contract_hash", new CLValue(ByteUtils.decodeHex(contracthash), new CLByteArrayInfo(32), contracthash))
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
