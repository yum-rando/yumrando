package com.yumrando.app.repos;

import com.yumrando.app.models.RestaurantTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository <RestaurantTag, Long> {
    RestaurantTag findById(long id);
}