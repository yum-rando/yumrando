package com.yumrando.app.repos;


import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListRestaurantRepository extends JpaRepository<ListRestaurant, Long> {
    ListRestaurant findById(long id);

    ListRestaurant save(ListRestaurant listRestaurant);

    ListRestaurant deleteByIdIn(List<Restaurant> restaurants);
    ListRestaurant findByName(String name);
    //String findByNameEquals(String name);
    ListRestaurant findAllByUserAndName(User user, String name);
    ListRestaurant deleteByName(String name);
    List<ListRestaurant> findAllByUser(User user);
    List<ListRestaurant> findAllByUserId(long id);
}
