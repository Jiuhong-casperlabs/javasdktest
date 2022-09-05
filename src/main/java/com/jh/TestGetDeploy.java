package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.types.Deploy;

import com.casper.sdk.types.*;

public class TestGetDeploy {
    public static void main(String args[]) {
        final CasperSdk casperSdk = new CasperSdk("http://52.70.214.247", 7777);
        
        Digest deployHash = new Digest("ac45d6a320382a7b89a2684a647adc19adca73af99a3a00c00d428026c4073c1");
        final Deploy deploy = casperSdk.getDeploy(deployHash);
    

        System.out.println(deploy.getHeader());
    }
}