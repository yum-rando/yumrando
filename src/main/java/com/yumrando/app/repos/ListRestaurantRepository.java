package com.yumrando.app.repos;


import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.ListRestaurantNameOnly;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ListRestaurantRepository extends JpaRepository<ListRestaurant, Long> {
    ListRestaurant findById(long id);

    //ListRestaurant save(ListRestaurant listRestaurant);
    ListRestaurant save(ListRestaurant list, String id);
    ListRestaurant save(ListRestaurantNameOnly listRestaurant, String id);

    //ListRestaurant deleteByIdIn(List<Restaurant> restaurants);
    ListRestaurant findByName(String name);
    //String findByNameEquals(String name);
    ListRestaurant findAllByUserAndName(User user, String name);
    ListRestaurant findAllByUserAndId(User user, long id);
    ListRestaurant deleteByName(String name);
    List<ListRestaurant> findAllByUser(User user);
    List<ListRestaurant> findAllByUserId(long id);
    Set<ListRestaurant> findAllByRestaurants(Restaurant restaurant);
}
