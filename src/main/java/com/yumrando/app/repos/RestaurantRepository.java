package com.yumrando.app.repos;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findById(long id);


    //List<Restaurant> findByOrderByChosenTimeDesc();

    Restaurant save(Restaurant restaurant);

    //List<Restaurant> findAllByListId(long id);
    //List<Restaurant> findAllByListId(long id);
}
