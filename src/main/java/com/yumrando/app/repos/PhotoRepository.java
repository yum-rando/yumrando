package com.yumrando.app.repos;

import com.yumrando.app.models.Photo;
import com.yumrando.app.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByReview(Review review);
    Photo findById(long id);
    @Transactional
    void deleteAllByReview(Review review);
    @Transactional
    void deletePhotosByReview(Review review);
}
