package com.ruan.helpdesk.resources.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ruan.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.ruan.helpdesk.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException ex, 
			HttpServletRequest request){
		
		StandardError error = new StandardError(System.currentTimeMillis(), 
				HttpStatus.NOT_FOUND.value(),
				"Object Not Found",
				ex.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException ex, 
			HttpServletRequest request){
		
		StandardError error = new StandardError(System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Violação de Dados",
				ex.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validationErrors (MethodArgumentNotValidException ex, 
			HttpServletRequest request){
		
		ValidationError errors = new ValidationError(System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Validation Error",
				"Erro na validação dos campos",
				request.getRequestURI());
		
		for(FieldError x : ex.getBindingResult().getFieldErrors()) {
			errors.addError(x.getField(), x.getDefaultMessage());
		}
			
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> dataIntegrityViolationException(ConstraintViolationException ex, HttpServletRequest request) {
		StandardError error = new StandardError(System.currentTimeMillis(), 
				HttpStatus.BAD_REQUEST.value(),
				"Violação de Dados",
				"Número do registro de contribuinte individual brasileiro (CPF) inválido",
				request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
