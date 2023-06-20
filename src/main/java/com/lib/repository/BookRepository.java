package com.lib.repository;

import com.lib.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book,Long> {


    Optional<Book> findBookById(Long bookId);

    @Query("select count(*) from Book b join b.imageFile im where im.id=:id")
    Integer findBookCountByImageId(@Param("id") String id);

    boolean existsByIsbn(String isbn);

}
