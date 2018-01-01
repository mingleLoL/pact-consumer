package com.ziroom.consumer;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.model.MockProviderConfig;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.junit.Assert.assertEquals;

/**
 * Sometimes it is not convenient to use the ConsumerPactTest as it only allows one test per test class.
 * The DSL can be used directly in this case.
 */
public class GetUserPactTest {

    @Test
    public void testPact() {
        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("cdc-consumer")
                .hasPactWith("cdc-provider")
                .uponReceiving("get a simple user json")
                .path("/user/getUser")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body("{\"ming\": \"123456\"}")
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = runConsumerTest(pact, config, mockServer -> {
            Map expectedResponse = new HashMap();
            expectedResponse.put("ming", "123456");
            try {
                assertEquals(new ProviderClient(mockServer.getUrl()).getUser(),
                        expectedResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (result instanceof PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error)result).getError());
        }

        assertEquals(PactVerificationResult.Ok.INSTANCE, result);
    }

}