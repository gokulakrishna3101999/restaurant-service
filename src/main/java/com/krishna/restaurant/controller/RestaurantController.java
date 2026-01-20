package com.krishna.restaurant.controller;

import com.krishna.restaurant.dto.RestaurantDTO;
import com.krishna.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
@CrossOrigin
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/get")
    ResponseEntity<List<RestaurantDTO>> getAllRestaurant() {
        List<RestaurantDTO> restaurants = restaurantService.findAll();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @PostMapping("/add")
    ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        return new ResponseEntity<>(restaurantService.add(restaurantDTO),HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable Integer id) {
        return new ResponseEntity<>(restaurantService.findById(id),HttpStatus.OK);
    }
}
