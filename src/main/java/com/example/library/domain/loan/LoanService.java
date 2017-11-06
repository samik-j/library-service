package com.example.library.domain.loan;

import com.example.library.domain.user.UserRepository;
import com.example.library.domain.edition.Edition;
import com.example.library.domain.edition.EditionRepository;
import com.example.library.web.loan.LoanOverdue;
import com.example.library.web.loan.LoanResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private EditionRepository editionRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, EditionRepository editionRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
    }

    @Transactional //robi transakcje czyli albo zrobi wszystko albo nic(wtedy jak poleci wyjatek)
    //przy Transactional metoda musi byc public
    //jezeli w tej klasie zawolam ta metode to nie bedzie tu transakcyjna
    public Loan registerLoan(LoanResource resource) {
        Edition edition = editionRepository.findOne(resource.getEditionId());

        edition.lend();
        editionRepository.save(edition);

        Loan loan = new Loan(userRepository.findOne(resource.getUserId()), editionRepository.findOne(resource.getEditionId()));

        return loanRepository.save(loan);
    }

    public boolean loanExists(long loanId) {
        return loanRepository.exists(loanId);
    }

    @Transactional
    public Loan returnLoan(long loanId) {
        Loan loan = loanRepository.findOne(loanId);

        loan.returnLoan();

        return loanRepository.save(loan);
    }

    public List<Loan> findLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> findLoans(boolean returned) {
        return loanRepository.findByReturned(returned);
    }

    public List<Loan> findLoans(LoanOverdue overdue) {
        if(overdue == LoanOverdue.NOW) {
            return loanRepository.findByDateToReturnBefore(LocalDate.now());
        }
        else if(overdue == LoanOverdue.SOON){
            return loanRepository.findByDateToReturnBefore(LocalDate.now().plusDays(5));
        }
        throw new UnsupportedLoanOverdueException();
    }

    private class UnsupportedLoanOverdueException extends RuntimeException {

    }

}