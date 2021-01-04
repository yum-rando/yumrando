package com.yumrando.app.repos;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository <RestaurantTag, Long> {
    RestaurantTag findById(long id);
    RestaurantTag findByIdAndAndUsers(long id, User user);

    RestaurantTag save(RestaurantTag tag);

}
