package com.example.library.domain.edition;

class InsufficientEditionQuantityException extends RuntimeException {

    InsufficientEditionQuantityException(long id) {
        super("Insufficient quantity for Edition with id " + id);
    }
}
