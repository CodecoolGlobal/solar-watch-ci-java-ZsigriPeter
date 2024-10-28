package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SolarWatchController {

    private final SolarWatchService solarWatchService;

    public SolarWatchController(SolarWatchService solarWatchService) {
        this.solarWatchService = solarWatchService;
    }

    @GetMapping("/getBy")
    @PreAuthorize("hasRole('USER')")
    public List<CityReport> getCurrent(@RequestParam LocalDate date, @RequestParam String city) {
        List<CityReport> result = solarWatchService.getCityReport(city, date);
        return result;
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public long deleteById(@RequestParam long id) {
        long result = solarWatchService.deleteCityReportById(id);
        return result;
    }


}
