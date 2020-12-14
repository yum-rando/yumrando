package com.yumrando.app.repos;

import com.yumrando.app.models.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListFriendsRepository extends JpaRepository <Friend, Long>{
    Friend findById(long id);
    Friend Save(Friend friendship);
}
