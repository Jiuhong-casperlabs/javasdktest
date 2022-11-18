package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.*;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;

import java.security.KeyPair;
import java.time.Instant;

import java.io.File;
import java.io.InputStream;

public class InstallCEP78 {
    public static void main(String[] args) {
        // final CasperSdk casperSdk = new CasperSdk("http://3.208.91.63", 7777);
        final CasperSdk casperSdk = new CasperSdk("http://117.107.141.162", 7777); //chendu chain
 
        final SigningService sss = new SigningService();

        // contract code github: https://github.com/Jiuhong-casperlabs/disable-enable-contract
        // contract wasm path => 
        final InputStream contractwasmIn = HowToUtils.getWasmIn("wasm/cep78.wasm");
        // chain name
        final String chainName = "chengdu";
        // payment
        final Number payment = 200e9;


        // Get operator keypair.
        File pkfile = new File("/home/jh/keys/chengduadmin/public_key.pem");
        File skfile = new File("/home/jh/keys/chengduadmin/secret_key.pem");
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
                                .add("collection_name", CLValueBuilder.string("AAA-NFT")) // 
                                .add("collection_symbol", CLValueBuilder.string("JH")) // u256 : '8'
                                .add("total_token_supply", CLValueBuilder.u64(10)) // u256 : '8'
                                .add("ownership_mode", CLValueBuilder.u8(2)) // u256 : '8'
                                .add("nft_kind", CLValueBuilder.u8(1)) // u256 : '8'
                                .add("holder_mode", CLValueBuilder.u8(2))
                                .add("json_schema", CLValueBuilder.string("nft-schema"))
                                .add("allow_minting", CLValueBuilder.bool(true))
                                .add("nft_metadata_kind", CLValueBuilder.u8(1))
                                .add("identifier_mode", CLValueBuilder.u8(0))
                                .add("metadata_mutability", CLValueBuilder.u8(0))
            // new DeployNamedArgBuilder()
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
