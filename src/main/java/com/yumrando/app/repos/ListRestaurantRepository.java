package com.yumrando.app.repos;


import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListRestaurantRepository extends JpaRepository<ListRestaurant, Long> {
    ListRestaurant findById(long id);
    ListRestaurant save(ListRestaurant listRestaurant);
    ListRestaurant deleteByIdIn(List<Restaurant> restaurants);
}
