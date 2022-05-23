package com.jh;

import com.casper.sdk.CasperSdk;
public class TestGetBlock {
    public static void main(String args[]) {

        final CasperSdk casperSdk = new CasperSdk("http://16.162.124.124", 7777);

        final String result = casperSdk.getBlockInfoByHeight(10000);

        System.out.println(result);
    }
}