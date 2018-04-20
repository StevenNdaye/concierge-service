package com.concierge.e2e;

import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerE2ETest {

    @Autowired
    private CityRepository cityRepository;

    @LocalServerPort
    private int port;

    @Test
    public void shouldFetchCityInformationByName() {

        City expectedCity = new City(1, "Johannesburg", "South Africa",
                "Fun Fact", "Description", 12.2, 23.4);

        cityRepository.save(expectedCity);

        when()
                .get(String.format("http://localhost:%s/cities?name=Johannesburg", port))
                .then()
                .statusCode(is(200));
    }

    @After
    public void tearDown() {
        cityRepository.deleteAll();
    }
}
