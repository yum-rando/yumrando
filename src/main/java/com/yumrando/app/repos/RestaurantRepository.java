package com.yumrando.app.repos;

import com.yumrando.app.models.ListRestaurant;
import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);

    Restaurant save(Restaurant restaurant);
    Restaurant findAllByApiId (String apiId);

    Set<Restaurant> findAllByLists (ListRestaurant list);

    //Search query that only relies on the zipcode and the name
    Restaurant findAllByZipcodeAndNameContaining(String zipcode, String restName);
}
