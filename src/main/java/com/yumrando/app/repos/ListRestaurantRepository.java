package com.yumrando.app.repos;


import com.yumrando.app.models.ListRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRestaurantRepository extends JpaRepository<ListRestaurant, Long> {
    ListRestaurant findById(long id);
}
