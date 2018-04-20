package com.concierge.integration.client;

import com.concierge.client.WeatherClient;
import com.concierge.domain.WeatherResponse;
import com.concierge.entity.City;
import com.concierge.helper.FileLoader;
import com.concierge.repository.CityRepository;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherClientIntegrationTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private WeatherClient weatherClient;

    @MockBean
    private CityRepository cityRepository;

    @Test
    public void shouldGetWeather() throws IOException {

        City expectedCity = new City(1, "Johannesburg", "South Africa",
                "Fun Fact", "Description", 53.5511, 9.9937);

        wireMockRule.stubFor(get(urlPathEqualTo("/some-test-api-key/53.5511,9.9937"))
                .willReturn(aResponse()
                        .withBody(FileLoader.read("classpath:weatherApiResponse.json"))
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)));

        given(cityRepository.findByName("Johannesburg"))
                .willReturn(Optional.of(expectedCity));

        Optional<WeatherResponse> expectedWeatherResponse = Optional.of(new WeatherResponse("Rain"));

        Optional<WeatherResponse> weatherResponse = weatherClient.fetchWeather("Johannesburg");

        assertThat(weatherResponse, is(expectedWeatherResponse));

        verify(1, getRequestedFor(urlEqualTo("/some-test-api-key/53.5511,9.9937")));
    }
}