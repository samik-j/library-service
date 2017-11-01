package com.example.library.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Set;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Set<Loan> findByDateToReturnBefore(LocalDate date);
}
