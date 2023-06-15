package com.lib.repository;

import com.lib.controller.PublisherController;
import com.lib.domain.Author;
import com.lib.domain.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {
    Optional<Publisher> findPublisherById(Long id);

    Page<Publisher> findAll(Pageable pageable);



}
