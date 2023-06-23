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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    @Query("select l from Loan l where l.expireDate<:now and l.user.id=:id ")
    List<Loan> findLoanExpiredByUser(@Param("id") Long id, @Param("now") LocalDateTime now);

    @Query("select l from Loan l where l.user.id=:id and l.expireDate=:null")
    List<Loan> findLoansByUserIdAndExpireDateIsNull(@Param("id") Long user);


    @EntityGraph(attributePaths = {"book", "book.imageFile","user"})
    Page<Loan> findAllLoansByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"book","book.imageFile"})
    Page<Loan> findAll(Pageable pageable);

    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"book","book.imageFile"})
    Page<Loan> findAllLoansByBook(Book book, Pageable pageable);
    @EntityGraph(attributePaths = {"book", "book.imageFile","user"})
    Optional<Loan> findLoanById(Long loanId);

    List<Loan> findByBookId(Long id);

    List<Loan> findAllByUserId(Long id);
}
