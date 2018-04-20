package com.concierge.client;

import com.concierge.domain.WeatherResponse;
import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class WeatherClient {

    private final String url;
    private String secret_key;
    private RestTemplate restTemplate;
    private CityRepository cityRepository;

    @Autowired
    public WeatherClient(@Value("${weather.url}") String url, @Value("${weather.api-secret}") String secret_key, RestTemplate restTemplate,
                         CityRepository cityRepository) {
        this.url = url;
        this.secret_key = secret_key;
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public Optional<WeatherResponse> fetchWeather(String name) {

        Optional<City> city = cityRepository.findByName(name);

        String urlParam = String.format("%s/%s/%s,%s", url, secret_key, city.get().getLatitude(), city.get().getLongitude());

        return Optional.of(restTemplate.getForObject(urlParam, WeatherResponse.class));
    }
}
