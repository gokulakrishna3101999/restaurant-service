package com.krishna.restaurant.service;

import com.krishna.restaurant.dto.RestaurantDTO;
import com.krishna.restaurant.entity.Restaurant;
import com.krishna.restaurant.mapper.RestaurantMapper;
import com.krishna.restaurant.repository.RestaurantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepo restaurantRepo;
    private final RestaurantMapper restaurantMapper;

    public List<RestaurantDTO> findAll() {
        List<Restaurant> restaurants = restaurantRepo.findAll();
        return restaurants.stream().map(restaurantMapper::mapRestaurantToRestaurantDTO).toList();
    }

    public RestaurantDTO add(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.mapRestaurantDTOToRestaurant(restaurantDTO);
        restaurant = restaurantRepo.save(restaurant);
        return restaurantMapper.mapRestaurantToRestaurantDTO(restaurant);
    }

    public RestaurantDTO findById(Integer id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(id);
        return restaurantMapper.mapRestaurantToRestaurantDTO(optionalRestaurant.orElseGet(Restaurant::new));
    }
}
