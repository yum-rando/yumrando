package com.yumrando.app.repos;

import com.yumrando.app.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);
    Restaurant save(Restaurant restaurant);
}
