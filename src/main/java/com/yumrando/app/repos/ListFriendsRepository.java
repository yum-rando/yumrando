package com.yumrando.app.repos;

import com.yumrando.app.models.FriendList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListFriendsRepository extends JpaRepository <FriendList, Long>{
    FriendList findById(long id);

    FriendList save(FriendList friendList);
    FriendList findAllByUserIdAndFriendId(long userId, long friendId);
    List<FriendList> findAllByUserId(long userId);
    List<FriendList> findAllByFriendId(long friendId);
    void deleteById(Long id);
}
