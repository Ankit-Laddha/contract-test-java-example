package io.ankitladdha.contracts;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerContractTest {

    private static final String echoRequest =
            "{\n" +
                    "    \"timestamp\": 1593373353,\n" +
                    "    \"greetings\": \"hello!\"\n" +
                    "}";

    private final String echoResponse =
            "{\n" +
                    "    \"phrase\": \"hello! sent at: 1593373353 worked at: 1621329667\"\n" +
                    "}";


    @BeforeEach
    public void setup(MockServer mockServer) {
        assertThat(mockServer).isNotNull();
    }

    @Pact(provider = "PhraseMicroservice", consumer = "ConsumerMicroservice")
    public RequestResponsePact testEchoPact(PactDslWithProvider builder) {
        return builder
                .given("a greetings req")
                .uponReceiving("an echo req")
                .path("/api/echo")
                .method("POST")
                .body(echoRequest)
                .willRespondWith()
                .status(200)
                .body(echoResponse)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "testEchoPact")
    void test(MockServer mockServer) throws IOException {
        BasicHttpEntity bodyRequest = new BasicHttpEntity();
        bodyRequest.setContent(IOUtils.toInputStream(echoRequest, Charset.defaultCharset()));

        HttpResponse response = Request.Post(mockServer.getUrl() + "/api/echo")
                .body(bodyRequest)
                .execute()
                .returnResponse();

        ObjectMapper objectMapper = new ObjectMapper();
        var result = objectMapper.readValue(response.getEntity().getContent(), EchoResponse.class);

        var expectedResult = new EchoResponse();
        expectedResult.setPhrase("hello! sent at: 1593373353 worked at: 1621329667");

        System.out.println("result = " + result);
        assertThat(expectedResult).isEqualTo(result);

    }

}
