package com.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
		String msg=ex.getMessage();
		ApiResponse res=new ApiResponse(msg,false);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
		Map<String, String> res=new HashMap<String, String>();
		
		ex.getBindingResult().getAllErrors().forEach(error->{
			String fieldName=((FieldError) error).getField();
			String msg=error.getDefaultMessage();
			res.put(fieldName, msg);
		});
		
		return new ResponseEntity<Map<String,String>>(res,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse> BadCredentialsExceptionHandler(BadCredentialsException ex){
		String msg=ex.getMessage();
		ApiResponse res=new ApiResponse(msg,false);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.NOT_FOUND);	
	}

}
