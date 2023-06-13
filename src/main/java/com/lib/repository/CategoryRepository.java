package com.lib.repository;

import com.lib.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {


    @Query("select c from Category c order by c.sequence desc" )
    List<Category> findBySequence();

    boolean existsBySequence(int sequence);




}
