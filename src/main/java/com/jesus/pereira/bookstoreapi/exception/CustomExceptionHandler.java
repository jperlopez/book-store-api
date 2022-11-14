package com.jesus.pereira.bookstoreapi.exception;


import com.jesus.pereira.bookstoreapi.domain.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(value = NoSuchElementExistsException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementExists(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .build();

        logger.info("Error found at: ".concat(errorResponse.getTimestamp().toString()));
        logger.info("Error message: ".concat(errorResponse.getError()));
        logger.info("Error message: ".concat(errorResponse.getError()));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(ex.getMessage())
                .build();

        logger.info("Error found at: ".concat(errorResponse.getTimestamp().toString()));
        logger.info("Error message: ".concat(errorResponse.getError()));
        logger.info("Error message: ".concat(errorResponse.getError()));

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = BookAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBookAlreadyExistsException(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(ex.getMessage())
                .build();

        logger.info("Error found at: ".concat(errorResponse.getTimestamp().toString()));
        logger.info("Error message: ".concat(errorResponse.getError()));
        logger.info("Error message: ".concat(errorResponse.getError()));

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AuthorAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAuthorAlreadyExistsException(Exception ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(ex.getMessage())
                .build();

        logger.info("Error found at: ".concat(errorResponse.getTimestamp().toString()));
        logger.info("Error message: ".concat(errorResponse.getError()));
        logger.info("Error message: ".concat(errorResponse.getError()));

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
