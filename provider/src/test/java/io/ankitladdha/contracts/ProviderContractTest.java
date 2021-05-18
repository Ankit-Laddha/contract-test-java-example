package io.ankitladdha.contracts;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import io.ankitladdha.contracts.service.PhraseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ProviderApplication.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-contract-test.properties")
@Provider("PhraseMicroservice")
@PactFolder("../consumer/pacts")
public class ProviderContractTest {

    @Value("${server.host}")
    private String serverHost;

    @Value("${server.port}")
    private int serverPort;

    @MockBean
    PhraseService phraseService;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget(serverHost, serverPort, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("a greetings req")
    public void greetingsWorkedAt1593373353() {
        when(phraseService.echo(1593373353, "hello!"))
                .thenReturn("hello! sent at: 1593373353 worked at: 1621329667");
    }

}

