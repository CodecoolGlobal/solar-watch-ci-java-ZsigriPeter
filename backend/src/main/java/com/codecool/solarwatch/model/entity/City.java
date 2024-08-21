package com.codecool.solarwatch.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
public class City {

    @Id
    @GeneratedValue
    private long id;
    @Getter
    private String name;
    @Getter
    private double longitude;
    @Getter
    private double latitude;
    @Getter
    private String country;
    @Getter
    private String state;

    @OneToMany(mappedBy = "city")
    private List<SunRiseSet> sunriseSet;

    public City(String name, double lon, double lat, String country, String state) {
    }

    public City() {}

    public SunRiseSet getSunriseSet(LocalDate date) {
        return sunriseSet.stream().filter(sunrise -> sunrise.getDate().equals(date)).findFirst().orElse(null);
    }
}
