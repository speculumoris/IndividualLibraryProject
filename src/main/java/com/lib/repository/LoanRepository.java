package com.lib.repository;

import com.lib.domain.Book;
import com.lib.domain.Loan;
import com.lib.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("select l from Loan l where l.expireDate<:now and l.user.id=:id ")
    List<Loan> findLoanExpiredByUser(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Query("select l from Loan l where l.user.id=:id and l.expireDate=:null")
    List<Loan> findLoansByUserIdAndExpireDateIsNull(@Param("id") Long user);


    @EntityGraph(attributePaths = {"book", "book.imageFile","user"})
    Page<Loan> findAllLoansByUserAndPage(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"book","book.imageFile"})
    Page<Loan> findAll(Pageable pageable);

    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"book","book.imageFile"})
    Page<Loan> findAllLoansByBookPage(Book book, Pageable pageable);
}
