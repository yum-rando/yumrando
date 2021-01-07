package com.yumrando.app.repos;

import com.yumrando.app.models.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    UserTag findById(long id);
    UserTag save(long id);
    UserTag deleteUserTagById(long id);
}
