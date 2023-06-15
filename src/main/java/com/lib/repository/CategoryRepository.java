package com.lib.repository;

import com.lib.domain.Author;
import com.lib.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsBySequence(int sequence);

    Page<Category> findAll(Pageable pageable);




}
