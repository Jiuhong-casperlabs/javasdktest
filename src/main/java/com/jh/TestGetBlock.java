package com.jh;

import com.casper.sdk.CasperSdk;
public class TestGetBlock {
    public static void main(String args[]) {

        final CasperSdk casperSdk = new CasperSdk("http://94.130.10.55", 7777);

        final String result = casperSdk.getBlockInfoByHeight(10000);

        System.out.println(result);
    }
}