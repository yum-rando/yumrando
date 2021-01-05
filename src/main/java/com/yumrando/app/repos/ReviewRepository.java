package com.yumrando.app.repos;

import com.yumrando.app.models.Review;
import com.yumrando.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
        Review findById(long id);
        Review findReviewByUserId(long id);
        Review findReviewByRestaurantId(long id);
        Review findReviewsByRating(int rating);
        List<Review> findAllByUser(User user);
        Review findReviewByUserIdAndRestaurantId(long userid, long restid);
        Review findReviewByUserIdAndId(long userId, long reviewId);
        List<Review> findAllByRestaurantId(long restaurantId);
        List<Review> findAllByOrderByUpdateTimeDesc();
}
