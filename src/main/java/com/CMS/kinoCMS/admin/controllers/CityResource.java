package com.CMS.kinoCMS.admin.controllers;

import com.CMS.kinoCMS.admin.models.City;
import com.CMS.kinoCMS.admin.repositories.CityRepository;
import com.CMS.kinoCMS.admin.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
public class CityResource {
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Autowired
    public CityResource(CityRepository cityRepository, UserRepository userRepository) {
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public City getCity(@PathVariable long id) {
        return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("City not found"));
    }

    @GetMapping("/all")
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityRepository.save(city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable long id) {
        cityRepository.deleteById(id);
    }

    @GetMapping("/{id}/user-count")
    public long getUserCountByCity(@PathVariable long id) {
        return userRepository.countByCityId(id);
    }
}
