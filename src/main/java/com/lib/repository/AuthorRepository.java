package com.lib.repository;

import com.lib.domain.Author;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Page<Author> findAll(Pageable pageable);

    Optional<Author> findAuthorById(Long id);


}
