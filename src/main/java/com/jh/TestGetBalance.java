package com.jh;

import com.casper.sdk.CasperSdk;

import java.math.BigInteger;
import java.security.PublicKey;

public class TestGetBalance {
    public static void main(String args[]) {
        System.out.println("Hello Java");
        // final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777); //testnet
        final CasperSdk casperSdk = new CasperSdk("http://3.14.161.135", 7777); //mainnet

        final PublicKey publicKey = casperSdk.createPublicKey("016e2e0fbe966e1bba69e4ba0c501687458615dbcd8d7f2dc76406d896bb6acae1");
        final BigInteger accountBalance = casperSdk.getAccountBalance(publicKey);
        System.out.println(accountBalance);
    }
}