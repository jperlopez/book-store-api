package com.jesus.pereira.bookstoreapi.exception;

public class BookAlreadyExistsException extends RuntimeException {

    public BookAlreadyExistsException(String message){
        super(message);
    }
}
