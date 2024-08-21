package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.report.CityReport;
import com.codecool.solarwatch.service.SolarWatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SolarWatchControllerTest {

    public SolarWatchController solarWatchController;

    @Mock
    public PasswordEncoder passwordEncoder;
    @Mock
    public AuthenticationManager authenticationManager;
    @Mock
    public SolarWatchService solarWatchServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        solarWatchController = new SolarWatchController(solarWatchServiceMock, passwordEncoder, authenticationManager);
    }

    @Test
    void getCurrentCorrectInput() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        String city = "Budapest";

        when(solarWatchServiceMock.getCityReport(city, date)).thenReturn(new CityReport(
                "5:58:31 AM",
                "6:08:07 PM",
                date,
                city
        ));

        String result = String.valueOf(solarWatchController.getCurrent(date, city));

        String expectedString = "<200 OK OK,CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest],[]>";

        assertEquals(expectedString, result);
    }

    @Test
    void getCurrentJustDate() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        //String city = "Budapest";

        when(solarWatchServiceMock.getCityReport(null, date)).thenThrow(new IllegalArgumentException(""));

        //String expectedString = "<200 OK OK,CityReport[sunrise=5:58:31 AM, sunset=6:08:07 PM, date=2020-01-01, city=Budapest],[]>";

        assertThrows(IllegalArgumentException.class, () -> solarWatchController.getCurrent(date, null));

    }
}
