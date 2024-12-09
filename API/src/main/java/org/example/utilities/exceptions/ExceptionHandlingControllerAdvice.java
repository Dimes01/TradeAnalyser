package org.example.utilities.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ErrorMessage> handleUsernameAlreadyExists(UsernameAlreadyExists e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content type", "application/json");
        return new ResponseEntity<>(
            new ErrorMessage(e.getMessage(), HttpStatus.BAD_REQUEST.value()),
            headers,
            HttpStatus.BAD_REQUEST
        );
    }
}
