package com.ziroom.consumer;

/**
 * @Author: yinm5
 * @Version: 1.0
 * @Date:2018/1/1
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProviderClient {

    private final String url;

    public ProviderClient(String url) {
        this.url = url;
    }

    public Map getUser() throws IOException {
        String response = Request.Get(url + "/user/getUser").execute().returnContent().asString();
        return new ObjectMapper().readValue(response, HashMap.class);
    }
}