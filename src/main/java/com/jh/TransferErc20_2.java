package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;

import java.security.KeyPair;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import java.io.File;
import static com.casper.sdk.service.serialization.util.ByteUtils.toByteArray;

public class TransferErc20_2 {
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
        final byte[] bytes =  ByteUtils.concat(toByteArray(CLKeyInfo.KeyType.ACCOUNT_ID.getTag()), ByteUtils.decodeHex(recipientHex));
        final CLKeyValue valueRecipient = new CLKeyValue(ByteUtils.lastNBytes(bytes, 32), CLKeyInfo.KeyType.valueOf(bytes[0]), null);
        final DeployNamedArg recipientArg = new DeployNamedArg("recipient", valueRecipient);

        // amount arg
        final CLValue valueAmount = new CLValue(new TypesFactory().getInstance(CLType.U256).serialize(8), CLType.U256, 8);
        final DeployNamedArg amountArg = new DeployNamedArg("amount", valueAmount);
     
        final List<DeployNamedArg> arguments = Arrays.asList(
            amountArg,
            recipientArg
            );
            
        final DeployExecutable executable = new StoredContractByHash(new ContractHash(contracthash),"transfer", arguments);

        // Set deploy.
        final Deploy deploy = casperSdk.makeDeploy(new DeployParams(
            operatorKeyPair.getPublic(),
            chainName,
            1,
            Instant.now().toEpochMilli(),
            DeployParams.DEFAULT_TTL,
            null),
            executable,
        casperSdk.standardPayment(payment));

        // Approve deploy.
        casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);
        System.out.println(digest);
    }
}
