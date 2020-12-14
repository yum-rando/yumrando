package com.yumrando.app.repos;

import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    public User findByUsername(String username);
//    public User Save(User user);
//    public User deleteById(long id);
}
