package com.example.library.domain.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT loan FROM Loan loan WHERE loan.dateToReturn < :date AND loan.returned = false")
    List<Loan> findByDateToReturnBefore(@Param("date") LocalDate date);

    @Query("SELECT loan FROM Loan loan WHERE loan.returned = :returned")
    List<Loan> findByReturned(@Param("returned") boolean returned);

    @Query("SELECT loan FROM Loan loan WHERE loan.edition.book.id = :bookId")
    List<Loan> findByBookId(@Param("bookId") long bookId);

}
