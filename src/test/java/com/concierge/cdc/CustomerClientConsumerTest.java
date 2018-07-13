package com.concierge.cdc;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.concierge.client.CustomerClient;
import com.concierge.domain.CustomerResponse;
import com.concierge.helper.FileLoader;
import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;



@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerClientConsumerTest {
    @Autowired
    private CustomerClient customerClient;

    @Rule
    public PactProviderRuleMk2 customerProvider = new PactProviderRuleMk2("customer_provider",
            "localhost", 8080, this);

    @Pact(consumer = "test_consumer", state = "test")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws IOException {
        return builder
                .given("customers data")
                .uponReceiving("a request for customers in a city")
                .path("/customers/filter?cityName=Johannesburg")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(FileLoader.read("classpath:customerApiResponse.json"), ContentType.APPLICATION_JSON)
                .toPact();

    }

    @Test
    @PactVerification("weather_provider")
    public void shouldFetchCustomers() throws Exception {
        CustomerResponse[] response = customerClient.fetchCustomers("Johannesburg");
        assertThat(response.length, greaterThan(0));
    }
}
