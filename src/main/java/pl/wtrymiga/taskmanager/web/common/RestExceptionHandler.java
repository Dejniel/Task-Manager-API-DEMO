package pl.wtrymiga.taskmanager.web.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.status(409).body(ex.getMessage());
	}

	@ExceptionHandler(ResponseStatusException.class)
	ResponseEntity<String> handleResponseStatus(ResponseStatusException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
	}
}
