package com.yumrando.app.repos;

import com.yumrando.app.models.FriendList;
import com.yumrando.app.models.FriendList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListFriendsRepository extends JpaRepository <FriendList, Long>{
    FriendList findById(long id);

}
