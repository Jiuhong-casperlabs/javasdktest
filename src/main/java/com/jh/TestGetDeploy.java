package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.types.Deploy;

import com.casper.sdk.types.*;

public class TestGetDeploy {
    public static void main(String args[]) {
        final CasperSdk casperSdk = new CasperSdk("http://94.130.10.55", 7777);
        
        Digest deployHash = new Digest("edcee8aeed84294f5fab7fa85a69816b329c254227112e999399170f734cbbc4");
        final Deploy deploy = casperSdk.getDeploy(deployHash);
    

        System.out.println(deploy.getHeader());
    }
}