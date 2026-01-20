package com.krishna.restaurant.mapper;

import com.krishna.restaurant.dto.RestaurantDTO;
import com.krishna.restaurant.entity.Restaurant;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);
    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);

}
