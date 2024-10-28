package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunRiseSet;
import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.model.report.OpenGeocodingReport;
import com.codecool.solarwatch.model.report.SunsetReport;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolarWatchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private SunsetRepository sunsetRepository;

    @InjectMocks
    private SolarWatchService solarWatchService;

    private City city;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        city = new City("London", -0.1278, 51.5074, "GB", "England");
        date = LocalDate.now();
    }

    @Test
    void testGetCityReportWhenCityExistsInRepo() {
        // Arrange
        List<City> cities = new ArrayList<>();
        cities.add(city);
        when(cityRepository.findByNameContaining("London")).thenReturn(cities);
        when(sunsetRepository.findByDateAndCityId(date, city.getId())).thenReturn(Optional.of(new SunRiseSet("06:00", "18:00", date, city)));

        // Act
        List<CityReport> reports = solarWatchService.getCityReport("London", date);

        // Assert
        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals("06:00", reports.get(0).sunrise());
        assertEquals("18:00", reports.get(0).sunset());
    }


    @Test
    void testDeleteCityReportByIdWhenExists() {
        // Arrange
        SunRiseSet sunriseSet = new SunRiseSet("06:00", "18:00", date, city);
        when(sunsetRepository.findById(city.getId())).thenReturn(Optional.of(sunriseSet));

        // Act
        long deletedId = solarWatchService.deleteCityReportById(city.getId());

        // Assert
        assertEquals(sunriseSet.getId(), deletedId);
        verify(sunsetRepository, times(1)).deleteById(city.getId());
    }

    @Test
    void testDeleteCityReportByIdWhenNotExists() {
        // Arrange
        when(sunsetRepository.findById(city.getId())).thenReturn(Optional.empty());

        // Act
        long deletedId = solarWatchService.deleteCityReportById(city.getId());

        // Assert
        assertEquals(-1, deletedId);
        verify(sunsetRepository, never()).deleteById(anyLong());
    }
//
//    @Test
//    void getCityReportForNewYork() {
//        String cityName = "New York";
//        String state = "US";
//        String country = "New York";
//        String sunRise="12:18:23 PM";
//        String sunSet="9:40:30 PM";
//        LocalDate date = LocalDate.of(2020, 1, 1);
//
//        when(solarWatchService.getCityReport(cityName,date)).thenReturn(List.of(new CityReport(
//                sunRise,
//                sunSet,
//                date,
//                cityName,
//                state,
//                country
//        )));
//
//        String result = String.valueOf(solarWatchService.getCityReport(cityName,date));
//
//        String expectedString = "<200 OK OK,[CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest, country=HU, state=Hungary]],[]>";
//
//        assertEquals(expectedString, result);
//    }
//
//    @Test
//    void getCityReportForLondon() {
//        //England	GB	London	2020-01-01	8:04:00 AM	4:03:40 PM
//        String cityName = "New York";
//        String state = "USA";
//        String country = "Hungary";
//        String sunRise="5:58:31 AM";
//        String sunSet="6:08:07 PM";
//        LocalDate date = LocalDate.of(2020, 1, 1);
//
//        when(solarWatchService.getCityReport(cityName,date)).thenReturn(Collections.singletonList(new CityReport(
//                sunRise,
//                sunSet,
//                date,
//                cityName,
//                state,
//                country
//        )));
//
//        String result = String.valueOf(solarWatchService.getCityReport(cityName,date));
//
//        String expectedString = "<200 OK OK,[CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest, country=HU, state=Hungary]],[]>";
//
//        assertEquals(expectedString, result);
//    }

}