package com.jh;

import com.casper.sdk.CasperSdk;

import java.math.BigInteger;
import java.security.PublicKey;

public class TestGetBalance {
    public static void main(String args[]) {
        // final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777); //testnet
        final CasperSdk casperSdk = new CasperSdk("http://94.130.10.55", 7777); //mainnet

        final PublicKey publicKey = casperSdk.createPublicKey("01d8956d47246fbe96f15114289d013a13452343d0a5316036f7bfe07ba49051a0");
        final BigInteger accountBalance = casperSdk.getAccountBalance(publicKey);
        System.out.println(accountBalance);
    }
}