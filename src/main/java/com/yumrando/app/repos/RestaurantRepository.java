package com.yumrando.app.repos;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);
    //Set<Restaurant> findAllByLists(ListRestaurant list);

    //List<Restaurant> findByOrderByChosenTimeDesc();

    Restaurant save(Restaurant restaurant);

    //List<Restaurant> findAllByListId(long id);
    //List<Restaurant> findAllByListId(long id);

    //Set<Restaurant> findAllByNameOrApiId(String restaurantName, String RestaurantApiId);
}
