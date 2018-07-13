package com.concierge.client;

import com.concierge.domain.CustomerResponse;
import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class CustomerClient {
    private final String url;
    private RestTemplate restTemplate;
    private CityRepository cityRepository;

    @Autowired
    public CustomerClient(@Value("${customer.url}") String url, RestTemplate restTemplate,
                          CityRepository cityRepository) {
        this.url = url;
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
    }

    public CustomerResponse[] fetchCustomers(String cityName) {

        Optional<City> city = cityRepository.findByName(cityName);

        String urlParam = String.format("%s/%s?cityName=%s", url, "customers/filter", city.get().getName());

        return restTemplate.getForObject(urlParam, CustomerResponse[].class);
    }
}
