package com.lib.repository;


import com.lib.domain.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {
    Optional<Publisher> findPublisherById(Long id);

    Page<Publisher> findAll(Pageable pageable);



}
