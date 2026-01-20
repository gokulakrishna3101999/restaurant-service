package com.krishna.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "restaurant_id")
    private Integer id;
    @Column(name = "restaurant_name")
    private String name;
    @Column(name = "restaurant_address")
    private String address;
    @Column(name = "restaurant_city")
    private String city;
    @Column(name = "restaurant_description")
    private String description;
}
