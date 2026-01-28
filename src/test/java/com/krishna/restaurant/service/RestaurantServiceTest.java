package com.krishna.restaurant.service;

import com.krishna.restaurant.dto.RestaurantDTO;
import com.krishna.restaurant.entity.Restaurant;
import com.krishna.restaurant.mapper.RestaurantMapper;
import com.krishna.restaurant.repository.RestaurantRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNull;

public class RestaurantServiceTest {
    @Mock
    RestaurantRepo restaurantRepo;

    @Mock
    RestaurantMapper restaurantMapper;

    @InjectMocks
    RestaurantService restaurantService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); //in order for Mock and InjectMocks annotations to take effect, you need to call MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllRestaurants() {
        // Create mock restaurants
        List<Restaurant> mockRestaurants = Arrays.asList(
                new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1"),
                new Restaurant(2, "Restaurant 2", "Address 2", "city 2", "Desc 2")
        );
        when(restaurantRepo.findAll()).thenReturn(mockRestaurants);
        // Call the service method
        List<RestaurantDTO> restaurantDTOList = restaurantService.findAll();
        // Verify the result
        assertEquals(mockRestaurants.size(), restaurantDTOList.size());
        for (int i = 0; i < mockRestaurants.size(); i++) {
            RestaurantDTO expectedDTO = restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurants.get(i));
            assertEquals(expectedDTO, restaurantDTOList.get(i));
        }
        // Verify that the repository method was called
        verify(restaurantRepo, times(1)).findAll();
    }

    @Test
    public void testAddRestaurantInDB() {
        // Create a mock restaurant to be saved
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        // Mock the mapper behavior
        when(restaurantMapper.mapRestaurantDTOToRestaurant(mockRestaurantDTO)).thenReturn(mockRestaurant);
        when(restaurantRepo.save(mockRestaurant)).thenReturn(mockRestaurant);
        when(restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurant)).thenReturn(mockRestaurantDTO);
        // Call the service method
        RestaurantDTO savedRestaurantDTO = restaurantService.add(mockRestaurantDTO);
        // Verify the result
        assertEquals(mockRestaurantDTO, savedRestaurantDTO);
        // Verify that the repository method was called
        verify(restaurantRepo, times(1)).save(mockRestaurant);
    }

    @Test
    public void testFetchRestaurantById_ExistingId() {
        // Create a mock restaurant ID
        Integer mockRestaurantId = 1;
        // Create a mock restaurant to be returned by the repository
        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
        // Mock the repository behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.of(mockRestaurant));
        when(restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurant)).thenReturn(mockRestaurantDTO);
        // Call the service method
        RestaurantDTO response = restaurantService.findById(mockRestaurantId);
        // Verify the response
        assertEquals(mockRestaurantId, response.getId());
        // Verify that the repository method was called
        verify(restaurantRepo, times(1)).findById(mockRestaurantId);
    }

    @Test
    public void testFetchRestaurantById_NonExistingId() {
        // Create a mock non-existing restaurant ID
        Integer mockRestaurantId = 1;
        // Mock the repository behavior
        when(restaurantRepo.findById(mockRestaurantId)).thenReturn(Optional.empty());
        // Call the service method
        RestaurantDTO response = restaurantService.findById(mockRestaurantId);
        // Verify the response
        assertNull(null, response);
        // Verify that the repository method was called
        verify(restaurantRepo, times(1)).findById(mockRestaurantId);
    }
}
