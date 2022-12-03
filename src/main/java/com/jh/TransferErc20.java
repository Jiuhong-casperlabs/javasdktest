package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;

import java.security.KeyPair;
import java.time.Instant;

import java.io.File;

public class TransferErc20 {
    public static void main(String[] args) {
        final CasperSdk casperSdk = new CasperSdk("http://94.130.10.55", 7777);
        final SigningService sss = new SigningService();

        // chain name
        final String chainName = "casper-test";
        // payment
        final Number payment = 3e9;


        // Get operator keypair.
        File pkfile = new File("/home/jh/keys/test2/public_key.pem");
        File skfile = new File("/home/jh/keys/test2/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);


        // contract_hash of erc20
        String contracthash = "c8ba1fabef349516319d21994f70dc88118599977a48f754690407c064726a67";

        // recipient key
        String recipientHex = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20";
        final byte[] key = ByteUtils.decodeHex(recipientHex);
        final CLValue CLValue_Recipient = CLValueBuilder.accountKey(key);
        
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
                "transfer",    // entrypoint
                new DeployNamedArgBuilder()
                                .add("amount", CLValueBuilder.u256(8))  // u256 : '8'
                                .add("recipient",CLValue_Recipient) // Key: account-hash-0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20
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
