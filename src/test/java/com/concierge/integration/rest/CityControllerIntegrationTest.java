package com.concierge.integration.rest;

import com.concierge.client.WeatherClient;
import com.concierge.controller.CityController;
import com.concierge.entity.City;
import com.concierge.repository.CityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CityController.class)
public class CityControllerIntegrationTest {

    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private WeatherClient weatherClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldRequestCityInformationByName() throws Exception {

        City expectedCity = new City(1, "Johannesburg", "South Africa",
                "Fun Fact", "Description", 12.2, 23.4);

        given(cityRepository.findByName("Johannesburg"))
                .willReturn(Optional.of(expectedCity));

        mockMvc.perform(get("/cities?name=Johannesburg"))
                .andExpect(content().json(expectedCity.toString()))
                .andExpect(status().is2xxSuccessful());
    }
}