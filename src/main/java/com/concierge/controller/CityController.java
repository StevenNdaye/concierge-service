package com.concierge.controller;

import com.concierge.client.WeatherClient;
import com.concierge.domain.WeatherResponse;
import com.concierge.entity.City;
import com.concierge.exception.CityNotFoundException;
import com.concierge.exception.ServiceUnavailableException;
import com.concierge.repository.CityRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/cities")
public class CityController {

    private CityRepository cityRepository;
    private WeatherClient weatherClient;

    public CityController(CityRepository cityRepository, WeatherClient weatherClient) {

        this.cityRepository = cityRepository;
        this.weatherClient = weatherClient;
    }

    @GetMapping(params = {"name"})
    public City fetchCityInformation(String name) {

        Optional<City> city = cityRepository.findByName(name);

        return city.orElseThrow(
                () -> new CityNotFoundException(String.format("The queried %s city does not exist in our system", name), name));
    }

    @GetMapping(value = "/weather", params = {"name"})
    public String fetchWeatherSummary(String name) {

        Optional<WeatherResponse> weather = weatherClient.fetchWeather(name);

        return weather.map(WeatherResponse::getSummary)
                .orElseThrow(() -> new ServiceUnavailableException("Weather service currently unavailable", name));
    }
}
