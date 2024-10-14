package com.codecool.solarwatch.repository;

import com.codecool.solarwatch.model.entity.SunRiseSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SunsetRepository extends JpaRepository<SunRiseSet, Long> {

    List<SunRiseSet> findByCityNameAndDate(String cityName, LocalDate date);

    Optional<SunRiseSet> findByDateAndCityId(LocalDate date, Long cityId);
}