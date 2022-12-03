package com.jh;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Base64;
import java.util.Map;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BalanceERC20 {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String writeValueAsString(final Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    public static void main(String[] args) throws IOException {
        // contract_hash of erc20
        String contracthash = "hash-4120116565bd608fae6a45078055f320a2f429f426c86797b072b4efd15b186a";

        // recipient key account-hash-9a770006ffda6f5b40f9f2752e8e82ee4c7f0dc11d1e83ecda5b1d25598195a9
        String recipientHex = "2293223427d59ebb331ac2221c3fcd1b3656a5cb72be924a6cdc9d52cdb6db0f";

        // step1: create dictionary item key
        final byte[] key = ByteUtils.decodeHex(recipientHex);

        byte[] head = { 00 };
        byte[] itemkey_bytes = new byte[key.length + 1];
        System.arraycopy(head, 0, itemkey_bytes, 0, head.length);
        System.arraycopy(key, 0, itemkey_bytes, head.length, key.length);

        String itemkey = Base64.getEncoder().encodeToString(itemkey_bytes);

        // step2 get dictionary value

        final CasperSdk casperSdk = new CasperSdk("http://94.130.10.55", 7777);
        final OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.get("application/json");

        // set rpc payload
        String stateRootHash = casperSdk.getStateRootHash();

        Map<Object, Object> contractNamedKey = CollectionUtils.Map.of(
                "key", contracthash,
                "dictionary_name", "balances",
                "dictionary_item_key", itemkey);
        Map<Object, Object> dictionary_identifier_value = CollectionUtils.Map.of("ContractNamedKey", contractNamedKey);

        Method method = new Method("state_get_dictionary_item",
                CollectionUtils.Map.of(
                        "state_root_hash", stateRootHash,
                        "dictionary_identifier", dictionary_identifier_value));
        final String content = writeValueAsString(method);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        final RequestBody body = RequestBody.create(bytes, JSON);
        final Request request = new Request.Builder()
                .url("http://94.130.10.55:7777/rpc")
                .header("Accept", "application/json")
                .post(body)
                .build();
        final Response response = client.newCall(request).execute();

        String result = response.body().string();
        System.out.println(result);

    }
}