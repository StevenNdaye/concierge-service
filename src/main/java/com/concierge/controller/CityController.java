package com.concierge.controller;

import com.concierge.entity.City;
import com.concierge.repository.CityRepository;

import java.util.Optional;

public class CityController {

    private CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {

        this.cityRepository = cityRepository;
    }

    public City fetchCityInformationById(int id) {

        Optional<City> city = cityRepository.findById(id);

        return city.get();
    }
}
