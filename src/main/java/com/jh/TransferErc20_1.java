package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import java.io.File;

public class TransferErc20_1 {
    public static void main(String[] args) {
        final CasperSdk casperSdk = new CasperSdk("http://3.208.91.63", 7777);
        final SigningService sss = new SigningService();

        // chain name
        final String chainName = "casper-test";
        // payment
        final Number payment = 3e9;


        // Get operator keypair.
        File pkfile = new File("/Users/jh/keys/test2/public_key.pem");
        File skfile = new File("/Users/jh/keys/test2/secret_key.pem");
        final KeyPair operatorKeyPair = sss.loadKeyPair(pkfile, skfile);


        // contract_hash of erc20
        String contracthash = "c8ba1fabef349516319d21994f70dc88118599977a48f754690407c064726a67";

        // recipient arg
        String recipientHex = "0102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f20";
        final byte[] key = ByteUtils.decodeHex(recipientHex);
        final CLValue CLValue_Recipient = CLValueBuilder.accountKey(key);
        
        final DeployNamedArg recipientArg = new DeployNamedArg("recipient", CLValue_Recipient);

        // amount arg
        final DeployNamedArg amountArg = new DeployNamedArg("amount", CLValueBuilder.u256(8));

        final List<DeployNamedArg> arguments = Arrays.asList(
            amountArg,
            recipientArg
            );

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
                arguments

        ),
        casperSdk.standardPayment(payment));

        // Approve deploy.
        casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}
