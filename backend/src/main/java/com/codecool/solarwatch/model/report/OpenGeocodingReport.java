package com.codecool.solarwatch.model.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenGeocodingReport(
        String name,
        double lat,
        double lon,
        String state,
        String country
) {
}
