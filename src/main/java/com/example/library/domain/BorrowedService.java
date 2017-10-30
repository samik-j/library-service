package com.example.library.domain;

import com.example.library.web.BorrowedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowedService{

    private BorrowedRepository borrowedRepository;
    private UserRepository userRepository;
    private EditionRepository editionRepository;

    @Autowired
    public BorrowedService(BorrowedRepository borrowedRepository, UserRepository userRepository, EditionRepository editionRepository) {
        this.borrowedRepository = borrowedRepository;
        this.userRepository = userRepository;
        this.editionRepository = editionRepository;
    }

    public List<Borrowed> findBorrowed() {
        return borrowedRepository.findAll();
    }

    public boolean registerBorrowed(BorrowedResource resource) {
        Edition edition = editionRepository.findOne(resource.getEditionId());
        boolean canBorrow = edition.borrow();
        editionRepository.save(edition);

        if(canBorrow) {
            Borrowed borrowed = new Borrowed(userRepository.findOne(resource.getUserId()), editionRepository.findOne(resource.getEditionId()));

            borrowedRepository.save(borrowed);

            return true;
        }

        return false;
    }
}
