package com.example.library.domain.edition;

public class InsufficientEditionQuantityException extends RuntimeException {

    public InsufficientEditionQuantityException(long id) {
        super("Insufficient quantity for Edition with id " + id);
    }
}
