package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.service.SolarWatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public SolarWatchController(SolarWatchService solarWatchService, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.solarWatchService = solarWatchService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/getBy")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CityReport>> getCurrent(@RequestParam LocalDate date, @RequestParam String city) {
        List<CityReport> result = solarWatchService.getCityReport(city, date);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CityReport> deleteById(@RequestParam long id) {
        CityReport result = solarWatchService.deleteCityReportById(id);
        return ResponseEntity.ok(result);
    }


}
