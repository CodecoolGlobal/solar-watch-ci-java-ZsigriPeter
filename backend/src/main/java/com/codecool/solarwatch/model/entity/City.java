package com.codecool.solarwatch.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
public class City {

    @Getter
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

    public City(String name, double longitude, double latitude, String country, String state) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
        this.state = state;
    }

    public City() {}

    public SunRiseSet getSunriseSet(LocalDate date) {
        return sunriseSet.stream().filter(sunrise -> sunrise.getDate().equals(date)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return "name: " + name + ", longitude: " + longitude + ", latitude: " + latitude+ ", country: " + country + ", state: " + state ;
    }
}
