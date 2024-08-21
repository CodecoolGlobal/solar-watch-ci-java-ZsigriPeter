package com.codecool.solarwatch.model.entity;

import com.codecool.solarwatch.model.report.CityReport;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
public class SunRiseSet {
    @Id
    @GeneratedValue
    private long id;
    @Getter
    private String sunrise;
    @Getter
    private String sunset;
    @Getter
    private LocalDate date;
    @Getter
    private String cityName;

    @ManyToOne
    private City city;

    public SunRiseSet(String sunrise, String sunset, LocalDate date, String cityName, City city) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.cityName = cityName;
        this.city = city;
    }

    public SunRiseSet() {

    }

    public CityReport getReport() {
      return new CityReport(this.getSunrise(), this.getSunset(), this.getDate(), this.getCityName());
    }
}
