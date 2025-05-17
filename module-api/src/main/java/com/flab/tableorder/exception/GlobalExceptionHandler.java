package com.flab.tableorder.exception;

import com.flab.tableorder.dto.ResponseDTO;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDTO> handleNotFound(NoHandlerFoundException ex) {
        ResponseDTO response = new ResponseDTO(404, "Requested resource not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({EntityNotFoundException.class, StoreNotFoundException.class, MenuNotFoundException.class})
    public ResponseEntity<ResponseDTO> handleEntityNotFound(RuntimeException ex) {
        ResponseDTO responseData = new ResponseDTO(404, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
    }
    @ExceptionHandler({PriceNotMatchedException.class})
    public ResponseEntity<ResponseDTO> handleEntityNotMatched(RuntimeException ex) {
        ResponseDTO responseData = new ResponseDTO(409, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseData);
    }
}
