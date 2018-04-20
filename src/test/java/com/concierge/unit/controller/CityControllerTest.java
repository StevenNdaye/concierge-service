package com.concierge.unit.controller;

import com.concierge.client.WeatherClient;
import com.concierge.controller.CityController;
import com.concierge.domain.WeatherResponse;
import com.concierge.entity.City;
import com.concierge.exception.CityNotFoundException;
import com.concierge.exception.ServiceUnavailableException;
import com.concierge.repository.CityRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CityControllerTest {

    private CityController cityController;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private WeatherClient weatherClient;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        initMocks(this);
        cityController = new CityController(cityRepository, weatherClient);
    }

    @Test
    public void shouldFetchCityInformationByName() {

        City expectedCity = new City(100, "Johannesburg", "South Africa",
                "Fun Fact", "Description", 12.2, 23.4);

        given(cityRepository.findByName("Johannesburg"))
                .willReturn(Optional.of(expectedCity));

        City fetchedCity = cityController.fetchCityInformation("Johannesburg");

        assertThat(fetchedCity, is(expectedCity));

        verify(cityRepository, times(1)).findByName("Johannesburg");
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenGivenInvalidName() {

        given(cityRepository.findByName("Johannesburg"))
                .willReturn(Optional.empty());

        exception.expect(CityNotFoundException.class);
        exception.expectMessage(String.format("The queried %s city does not exist in our system", "Johannesburg"));

        cityController.fetchCityInformation("Johannesburg");

        verify(cityRepository, times(1)).findByName("Johannesburg");
    }

    @Test
    public void shouldFetchWeatherSummaryWhenGivenCityName() {

        WeatherResponse weatherResponse = new WeatherResponse("Johannesburg, 20 raining");

        given(weatherClient.fetchWeather("Johannesburg"))
                .willReturn(Optional.of(weatherResponse));

        String summary = cityController.fetchWeatherSummary("Johannesburg");

        assertThat(summary, is("Johannesburg, 20 raining"));

        verify(weatherClient, times(1)).fetchWeather("Johannesburg");
    }

    @Test
    public void shouldThrowExceptionWhenWeatherServiceUnavailable() {

        given(weatherClient.fetchWeather("Johannesburg"))
                .willReturn(Optional.empty());

        exception.expect(ServiceUnavailableException.class);
        exception.expectMessage("Weather service currently unavailable");

        cityController.fetchWeatherSummary("Johannesburg");

        verify(weatherClient, times(1)).fetchWeather("Johannesburg");
    }
}