package com.concierge.unit;

import com.concierge.controller.CityController;
import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CityControllerTest {

    private CityController cityController;

    @Mock
    private CityRepository cityRepository;

    @Before
    public void setUp() {
        initMocks(this);
        cityController = new CityController(cityRepository);
    }

    @Test
    public void shouldFetchInformationForGivenCity() {

        City expectedCity = new City(100, "Johannesburg", "South Africa", "Fun Fact",
                "Description", 123.3, 456.9);

        given(cityRepository.findById(100))
                .willReturn(Optional.of(expectedCity));

        City city = cityController.fetchCityInformationById(100);

        assertThat(city, is(expectedCity));

        verify(cityRepository, times(1)).findById(100);
    }
}