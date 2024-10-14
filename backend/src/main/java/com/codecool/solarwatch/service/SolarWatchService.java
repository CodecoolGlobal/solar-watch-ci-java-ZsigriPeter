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
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.List;

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

    private List<City> getCitiesFromNameCoordinates(String cityName) throws IOException {
        String urlForLatLon = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s", cityName, API_KEY);

        ResponseEntity<OpenGeocodingReport[]> response =
                restTemplate.getForEntity(
                        urlForLatLon,
                        OpenGeocodingReport[].class);
        OpenGeocodingReport[] openGeocodingReports = response.getBody();

        logger.info("Response from Open Weather Geo Lat,Lon API: {}", urlForLatLon);
        assert openGeocodingReports != null;
        List<OpenGeocodingReport> result = Arrays.stream(openGeocodingReports).toList();

        List<City> cityList = new ArrayList<>();
        result.stream().forEach(city -> {
            if (cityRepository.findByLatitudeAndLongitude(city.lat(), city.lon()).isEmpty()) {
                City cityEntity = new City(city.name(), city.lon(), city.lat(), city.country(), city.state());
                cityRepository.save(cityEntity);
                cityList.add(cityEntity);
            } else {
                cityList.add(cityRepository.findByLatitudeAndLongitude(city.lat(), city.lon()).get());
            }
        });
        return cityList;
    }

    private List<CityReport> getCityReportListFromCityNameAndDate(String cityName, LocalDate date) {
        try {
            List<City> cityList = getCitiesFromNameCoordinates(cityName);
            List<CityReport> cityReportList = new ArrayList<>();
            cityList.stream().forEach(city -> {
                        SunRiseSet result = getSunriseSetFromAPIWithCityObject(city, date);
                        cityReportList.add(result.getReport());
                    }
            );
            return cityReportList;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private SunRiseSet getSunriseSetFromAPIWithCityObject(City city, LocalDate date) {
        if (sunsetRepository.findByDateAndCityId(date, city.getId()).isEmpty()) {
            String url = String.format("https://api.sunrise-sunset.org/json?lat=%s&lng=%s&date=%s", city.getLatitude(), city.getLongitude(), date);
            SunsetReport response = restTemplate.getForObject(url, SunsetReport.class);
            logger.info("Response from Sunrise-sunset API: {}", url);
            assert response != null;
            SunRiseSet result = new SunRiseSet(response.results().sunrise(), response.results().sunset(), date, city);
            sunsetRepository.save(result);
            return result;
        }
        return sunsetRepository.findByDateAndCityId(date, city.getId()).get();
    }

    public List<CityReport> getCityReport(String cityName, LocalDate date) {

        List<City> cityList = cityRepository.findByNameContaining(cityName);
        List<SunRiseSet> cityReportList = new ArrayList<>();
        cityList.forEach(city -> sunsetRepository.findByDateAndCityId(date, city.getId())
                .ifPresent(cityReportList::add));
        if (cityList.isEmpty()) {
            return getCityReportListFromCityNameAndDate(cityName, date);
        } else if (cityList.size() != cityReportList.size()) {
            List<SunRiseSet> cityReportListFromAPI = cityList.stream().map(city -> getSunriseSetFromAPIWithCityObject(city, date)).toList();
            return cityReportListFromAPI.stream().map(SunRiseSet::getReport).toList();
        }
        return cityReportList.stream().map(SunRiseSet::getReport).toList();
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

