package com.lib.repository;

import com.lib.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book,Long> {


    @EntityGraph(attributePaths = {"imageFile"})
    Optional<Book> findBookById(Long bookId);

    @Query("select count(*) from Book b join b.imageFile im where im.id=:id")
    Integer findBookCountByImageId(@Param("id") String id);

    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = {"imageFile"})
    Page<Book> findAll(Pageable pageable);


    @Query("select b from Book b join b.imageFile img where img.id=:id")
    List<Book> findBookByImageId(@Param("id") String id);
}
