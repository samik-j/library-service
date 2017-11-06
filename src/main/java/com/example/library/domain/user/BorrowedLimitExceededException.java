package com.example.library.domain.user;

class BorrowedLimitExceededException extends RuntimeException {

    BorrowedLimitExceededException(long id) {
        super("Exceeded borrowing limit for user with id " + id);
    }
}
