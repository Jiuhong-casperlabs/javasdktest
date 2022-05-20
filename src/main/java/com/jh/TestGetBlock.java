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

public class TestGetBlock {
    public static void main(String args[]) {
        System.out.println("Hello Java");
        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);

        final SigningService sss = new SigningService();
        // File pkfile = new File("E:\\learning\\java\\test\\keys\\test1\\test1_public_key.pem");
        // 
        File pkfile = new File("/home/jh/keys/test1/public_key.pem");
        
        // File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        // "/home/jh/keys/test1/secret_key.pem"
        File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        final KeyPair kp = sss.loadKeyPair(pkfile, skfile);
        
        File pkfile1 = new File("/home/jh/keys/test77/public_key.pem");
        
        // File skfile = new File("/home/jh/keys/test1/secret_key.pem");
        // "/home/jh/keys/test1/secret_key.pem"
        File skfile1 = new File("/home/jh/keys/test77/secret_key.pem");
        final KeyPair kp1 = sss.loadKeyPair(pkfile1,skfile1);

        final PublicKey publicKey = kp.getPublic();
        final PublicKey publicKey1 = kp1.getPublic();


        final Transfer transfer = casperSdk.newTransfer(
            new BigInteger("2500000000"),
            publicKey1,
                1
        );

        final String result = casperSdk.getBlockInfoByHeight(10000);

        // final String result_hash = casperSdk.getBlockInfo("a1f829cff2389cf6637ed89fb2fab48351b1278c131ee8445e1e28333c9a44d0");
        

        System.out.println(result);
    }
}