package com.example.pastebin.exception.handlers;

import com.example.pastebin.exception.BadParamException;
import com.example.pastebin.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(BadParamException.class)
    public ResponseEntity<?> badParam() {
        return ResponseEntity.status(400).build();
    }
}
