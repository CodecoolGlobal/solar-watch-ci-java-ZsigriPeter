package com.codecool.solarwatch.service;

import com.codecool.solarwatch.model.entity.City;
import com.codecool.solarwatch.model.entity.SunRiseSet;
import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.model.report.OpenGeocodingReport;
import com.codecool.solarwatch.model.report.SunsetReport;
import com.codecool.solarwatch.repository.CityRepository;
import com.codecool.solarwatch.repository.SunsetRepository;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.time.LocalDate;

import org.slf4j.Logger;

@Service
public class SolarWatchService {

    private static final String API_KEY = "937163359d1cdc16980dea3b0bf2b93e";

    private final CityRepository cityRepository;
    private final SunsetRepository sunsetRepository;
    private final RestTemplate restTemplate;

    public SolarWatchService(RestTemplate restTemplate, CityRepository cityRepository, SunsetRepository sunsetRepository) {
        this.restTemplate = restTemplate;
        this.cityRepository = cityRepository;
        this.sunsetRepository = sunsetRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(SolarWatchService.class);

    //https://api.openweathermap.org/geo/1.0/direct?q=London&limit=5&appid=937163359d1cdc16980dea3b0bf2b93e

    private City getFirstCityCoordinates(String cityName) throws IOException {
        String urlForLatLon = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s", cityName, API_KEY);

        ResponseEntity<OpenGeocodingReport[]> response =
                restTemplate.getForEntity(
                        urlForLatLon,
                        OpenGeocodingReport[].class);
        OpenGeocodingReport[] openGeocodingReports = response.getBody();

        logger.info("Response from Open Weather Geo Lat,Lon API: {}", urlForLatLon);
        assert openGeocodingReports != null;
        OpenGeocodingReport result = Arrays.stream(openGeocodingReports).toList().get(0);
        System.out.println(result);
        City city = new City(result.name(), result.lon(), result.lat(), result.country(), result.state());
        cityRepository.save(city);
        return Arrays.stream(openGeocodingReports).toList().isEmpty() ? null : city;
    }

    private SunRiseSet getCityReportFromAPI(String cityName, LocalDate date) {
        try {
            City city = getFirstCityCoordinates(cityName);
//            System.out.println("Report GEO: ");
//            System.out.println(openGeocodingReport);
            String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", city != null ? city.getLatitude() : 0, city != null ? city.getLongitude() : 0, date);
//            System.out.println(url);
            SunsetReport response = restTemplate.getForObject(url, SunsetReport.class);
            logger.info("Response from Sunrise-sunset API: {}", url);
//            System.out.println("Report Sunrise-sunset API: ");
//            System.out.println(response);
            assert response != null;
            //(String sunrise, String sunset, LocalDate date, String cityName, City city)
            SunRiseSet result = new SunRiseSet(response.results().sunrise(), response.results().sunset(), date, cityName, city);
            sunsetRepository.save(result);
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public CityReport getCityReport(String cityName, LocalDate date) {
        if (sunsetRepository.findByCityNameAndDate(cityName, date).isEmpty()) {
            SunRiseSet sunRiseSet = getCityReportFromAPI(cityName, date);
            return sunRiseSet.getReport();
        }
        return sunsetRepository.findByCityNameAndDate(cityName, date).get().getReport();
    }

    public CityReport deleteCityReportById(long cityId) {
        if (sunsetRepository.findById(cityId).isEmpty()) {
            return null;
        }
        SunRiseSet sunRiseSet = sunsetRepository.findById(cityId).get();
        sunsetRepository.deleteById(cityId);
        return sunRiseSet.getReport();
    }

}

