package com.codecool.solarwatch.repository;


import com.codecool.solarwatch.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByNameContaining(String name);
    Optional<City> findByLatitudeAndLongitude(double latitude, double longitude);
}