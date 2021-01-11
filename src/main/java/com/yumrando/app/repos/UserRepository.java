package com.yumrando.app.repos;

import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findById(long id);
    User save(User user);
    User deleteById(long id);
}
