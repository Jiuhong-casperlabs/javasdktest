package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.*;
import com.casper.sdk.exceptions.CasperException;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;

import org.apache.commons.io.IOUtils;

import java.security.KeyPair;
import java.io.IOException;
import java.time.Instant;
import java.io.InputStream;
import java.io.File;


final class HowToUtils {
     static InputStream getWasmIn(final String wasmPath) {
        return HowToUtils.class.getResourceAsStream(wasmPath);
    }

    public static byte[] readWasm(final InputStream wasmIn) {
        try {
            return IOUtils.toByteArray(wasmIn);
        } catch (IOException e) {
            throw new CasperException("Error loading wasm", e);
        }
    }
}

public class EnableAccount {
    public static void main(String args[]) {
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);
        final SigningService sss = new SigningService();

        // contract wasm path
        final InputStream contractwasmIn = HowToUtils.getWasmIn("wasm/set_action_thresholds.wasm");

        // chain name
        final String chainName = "mynetwork";
        // payment
        final Number payment = 3e9;

        // Get admin keypair.
        File pkfile = new File("/Users/jh/keys/test1/public_key.pem");
        File skfile = new File("/Users/jh/keys/test1/secret_key.pem");
        final KeyPair adminKeyPair = sss.loadKeyPair(pkfile, skfile);

        // Get user (to be enable) keypair.
        File pkfile1 = new File("/Users/jh/keys/test3/public_key.pem");
        File skfile1 = new File("/Users/jh/keys/test3/secret_key.pem");
        final KeyPair userKeyPair = sss.loadKeyPair(pkfile1, skfile1);

        // Set deploy.
        final Deploy deploy = casperSdk.makeDeploy(new DeployParams(
            userKeyPair.getPublic(),
            chainName,
            1,
            Instant.now().toEpochMilli(),
            DeployParams.DEFAULT_TTL,
            null),
            new ModuleBytes(HowToUtils.readWasm(contractwasmIn),
                        new DeployNamedArgBuilder()
                                .add("key_management_threshold", CLValueBuilder.u8(0))
                                .add("deploy_threshold", CLValueBuilder.u8(1))
                                .build()
                ),
            casperSdk.standardPayment(payment)
    );

        // Approve deploy.
        casperSdk.signDeploy(deploy, adminKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}
