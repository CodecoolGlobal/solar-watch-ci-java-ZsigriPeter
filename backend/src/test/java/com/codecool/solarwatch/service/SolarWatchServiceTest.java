package com.codecool.solarwatch.service;

import com.codecool.solarwatch.controller.SolarWatchController;
import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.model.report.SunsetReport;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SolarWatchServiceTest {

    public SolarWatchService solarWatchService;

    @Mock
    public RestTemplate restTemplate;
    @Mock
    public CityRepository cityRepository;
    @Mock
    public SunsetRepository sunsetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        solarWatchService = new SolarWatchService(restTemplate,cityRepository,sunsetRepository);
    }

    @Test
    void getCityReportForNewYork() {
        String cityName = "New York";
        String state = "USA";
        String country = "Hungary";
        String sunRise="5:58:31 AM";
        String sunSet="6:08:07 PM";
        LocalDate date = LocalDate.of(2020, 1, 1);

        when(solarWatchService.getCityReport(cityName,date)).thenReturn(Collections.singletonList(new CityReport(
                sunRise,
                sunSet,
                date,
                cityName,
                state,
                country
        )));

        String result = String.valueOf(solarWatchService.getCityReport(cityName,date));

        String expectedString = "<200 OK OK,[CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest, country=HU, state=Hungary]],[]>";

        assertEquals(expectedString, result);
    }

    @Test
    void getCityReportForLondon() {
        String cityName = "New York";
        String state = "USA";
        String country = "Hungary";
        String sunRise="5:58:31 AM";
        String sunSet="6:08:07 PM";
        LocalDate date = LocalDate.of(2020, 1, 1);

        when(solarWatchService.getCityReport(cityName,date)).thenReturn(Collections.singletonList(new CityReport(
                sunRise,
                sunSet,
                date,
                cityName,
                state,
                country
        )));

        String result = String.valueOf(solarWatchService.getCityReport(cityName,date));

        String expectedString = "<200 OK OK,[CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest, country=HU, state=Hungary]],[]>";

        assertEquals(expectedString, result);
    }

}