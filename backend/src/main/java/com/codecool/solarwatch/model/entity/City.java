package com.codecool.solarwatch.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
public class City {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private double longitude;
    private double latitude;
    private String country;
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

    @Override
    public String toString() {
        return "name: " + name + ", longitude: " + longitude + ", latitude: " + latitude+ ", country: " + country + ", state: " + state ;
    }
}
