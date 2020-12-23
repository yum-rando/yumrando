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

    //Set<Restaurant> findAllByApiIdOrName(String restaurantApiId, String RestaurantName);
    //Set<Restaurant>findAllByApiIdOrNameAndAddressAndZipcode(String apiId, String name, String address, String zipcode);
}
