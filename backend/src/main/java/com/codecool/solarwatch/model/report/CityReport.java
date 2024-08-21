package com.codecool.solarwatch.model.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CityReport(
        String sunrise,
        String sunset,
        LocalDate date,
        String city) { }
