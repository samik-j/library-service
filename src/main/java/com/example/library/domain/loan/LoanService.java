package com.example.library.domain.loan;

import com.example.library.domain.edition.Edition;
import com.example.library.domain.edition.EditionRepository;
import com.example.library.domain.user.User;
import com.example.library.domain.user.UserRepository;
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
        User user = userRepository.findOne(resource.getUserId());

        edition.lend();
        editionRepository.save(edition);
        user.borrow();
        userRepository.save(user);

        Loan loan = new Loan(userRepository.findOne(resource.getUserId()), editionRepository.findOne(resource.getEditionId()));

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnLoan(long loanId) {
        Loan loan = loanRepository.findOne(loanId);
        User user = userRepository.findOne(loan.getUser().getId());
        Edition edition = editionRepository.findOne(loan.getEdition().getId());

        loan.returnLoan();
        user.returnEdition();
        edition.returnEdition();

        userRepository.save(user);
        editionRepository.save(edition);

        return loanRepository.save(loan);
    }

    public Loan findLoan(long loanId) {
        return loanRepository.findOne(loanId);
    }

    public List<Loan> findLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> findLoans(boolean returned) {
        return loanRepository.findByReturned(returned);
    }

    public List<Loan> findLoans(long bookId) {
        return loanRepository.findByBookId(bookId);
    }

    public List<Loan> findLoans(LoanOverdue overdue) {
        if (overdue == LoanOverdue.NOW) {
            return loanRepository.findByDateToReturnBefore(LocalDate.now());
        } else if (overdue == LoanOverdue.SOON) {
            return loanRepository.findByDateToReturnBefore(LocalDate.now().plusDays(5));
        }
        throw new UnsupportedLoanOverdueException();
    }

    public boolean canBeReturned(long loanId) {
        return !loanRepository.findOne(loanId).isReturned();
    }

    public boolean loanExists(long loanId) {
        return loanRepository.exists(loanId);
    }

    private class UnsupportedLoanOverdueException extends RuntimeException {

    }

}
