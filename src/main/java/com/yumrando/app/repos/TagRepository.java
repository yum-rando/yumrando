package com.yumrando.app.repos;

import com.yumrando.app.models.Restaurant;
import com.yumrando.app.models.RestaurantTag;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository <RestaurantTag, Long> {
    List<RestaurantTag> findAll();
    RestaurantTag findById(long id);
    RestaurantTag findByName(String name);
    RestaurantTag findByIdAndUsersId(long tagId, long id);
    List<RestaurantTag> findAllByUsersId(long usersId);

    RestaurantTag save(RestaurantTag tag);

}
