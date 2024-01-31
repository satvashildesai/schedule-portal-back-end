package com.storeshop.scheduleportal.exceptions;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.storeshop.scheduleportal.util.ErrorResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RequestNotValidException.class)
	public Object handleRequestNotValidException(RequestNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FailedToSaveException.class)
	public Object handleFailedToSaveStaffMemberException(FailedToSaveException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("500", "INTERNAL_SERVER_ERROR", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public Object handleStaffMemberNotExistsException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", "NOT_FOUND", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ResourceAlreadyAssignedException.class)
	public Object handleShiftAlreadyScheduleException(ResourceAlreadyAssignedException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidStaffCountException.class)
	public Object handleInvalidStaffCountException(InvalidStaffCountException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", ex.getMessage()));
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(PasswordMismatchException.class)
	public Object handlePasswordMismatchException(PasswordMismatchException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("403", "FORBIDDEN", ex.getMessage()));
	}

//---------------------------------------- Handle predefined exceptions ------------------------------------------

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NumberFormatException.class)
	public Object handleNumberFormatException(NumberFormatException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", "Enter valid staff id in valid format (number)"));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(SQLException.class)
	public Object handleSQLException(SQLException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", "Can't delete. Given resource is scheduled."));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		StringBuilder msg = new StringBuilder();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			msg.append(error.getDefaultMessage() + " ");
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", msg.toString()));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DateTimeParseException.class)
	public Object handleHttpMessageNotReadableException(DateTimeParseException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", "Date format must be 'yyyy-mm-dd'"));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Object handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("400", "BAD_REQUEST", ex.getMessage()));
	}

//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	@ExceptionHandler(AuthenticationException.class)
//	public Object handleAuthenticationException(AuthenticationException ex) {
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body(new ErrorResponse("401", "UNAUTHORIZED", ex.getMessage()));
//	}
//
//	@ResponseStatus(HttpStatus.UNAUTHORIZED)
//	@ExceptionHandler(InsufficientAuthenticationException.class)
//	public Object handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body(new ErrorResponse("401", "UNAUTHORIZED", ex.getMessage()));
//	}

}
