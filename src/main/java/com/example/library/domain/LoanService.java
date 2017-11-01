package com.example.library.domain;

import com.example.library.web.LoanResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Loan> findLoans() {
        return loanRepository.findAll();
    }

    @Transactional //robi transakcje czyli albo zrobi wszystko albo nic(wtedy jak poleci wyjatek)
    //przy Transactional metoda musi byc public
    //jezeli w tej klasie zawolam ta metode to nie bedzie tu transakcyjna
    public boolean registerLoan(LoanResource resource) {
        Edition edition = editionRepository.findOne(resource.getEditionId());
        boolean canLend = edition.lend();
        editionRepository.save(edition);

        if(canLend) {
            Loan loan = new Loan(userRepository.findOne(resource.getUserId()), editionRepository.findOne(resource.getEditionId()));

            loanRepository.save(loan);

            return true;
        }

        return false;
    }
}
