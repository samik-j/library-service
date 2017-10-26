package com.example.library.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditionService {

    private EditionRepository repository;

    @Autowired
    public EditionService(EditionRepository repository) {
        this.repository = repository;
    }

    public List<Edition> findAll() {
        return this.repository.findAll();
    }

}
