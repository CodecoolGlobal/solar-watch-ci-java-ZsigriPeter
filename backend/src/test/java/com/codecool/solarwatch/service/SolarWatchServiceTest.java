package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunRiseSet;
import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
}