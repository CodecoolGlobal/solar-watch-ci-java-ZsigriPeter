package com.codecool.solarwatch.model.entity;

import com.codecool.solarwatch.model.report.CityReport;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class SunRiseSet {
    @Id
    @GeneratedValue
    private long id;
    private String sunrise;
    private String sunset;
    private LocalDate date;

    @ManyToOne
    private City city;

    public SunRiseSet(String sunrise, String sunset, LocalDate date, City city) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.date = date;
        this.city = city;
    }

    public SunRiseSet() {

    }

    public CityReport getReport() {
      return new CityReport(this.getSunrise(), this.getSunset(), this.getDate(),
              this.city.getName(),this.city.getCountry(), this.city.getState());
    }
}
