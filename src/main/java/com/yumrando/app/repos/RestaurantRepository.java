package com.yumrando.app.repos;

import com.yumrando.app.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);

    List<Restaurant> findByOrderByChosenTimeDesc();
}
