package com.flab.tableorder.exception;

import com.flab.tableorder.dto.ResponseDTO;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ResponseDTO> handleEntityNotFound(EntityNotFoundException ex) {
		ResponseDTO responseData = new ResponseDTO(404, ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
	}
}