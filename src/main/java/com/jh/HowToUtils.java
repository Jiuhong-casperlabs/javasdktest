package com.jh;
import java.io.IOException;
import com.casper.sdk.exceptions.CasperException;

import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public class HowToUtils {
    
    public static InputStream getWasmIn(final String wasmPath) {
        return HowToUtils.class.getResourceAsStream(wasmPath);
    }

    static byte[] readWasm(final InputStream wasmIn) {
        try {
            return IOUtils.toByteArray(wasmIn);
        } catch (IOException e) {
            throw new CasperException("Error loading wasm", e);
        }
    }
}
