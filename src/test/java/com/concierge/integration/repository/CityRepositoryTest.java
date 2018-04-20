package com.concierge.integration.repository;

import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    public void shouldFetchCityByName() {
        City expectedCity = new City(1, "Johannesburg", "South Africa",
                "Fun Fact", "Description", 12.2, 23.4);

        cityRepository.save(expectedCity);

        Optional<City> foundCity = cityRepository.findByName("Johannesburg");

        assertThat(foundCity, is(Optional.of(expectedCity)));
    }


    @After
    public void tearDown() throws Exception {
        cityRepository.deleteAll();
    }
}