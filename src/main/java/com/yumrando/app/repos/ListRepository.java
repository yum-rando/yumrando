package com.yumrando.app.repos;

import com.yumrando.app.models.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {
    List findById(long id);
}
