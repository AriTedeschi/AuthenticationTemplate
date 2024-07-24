package internal.management.accounts.config.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(
            ValidationException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserCodeOverflowException.class)
    public ResponseEntity<Object> handleUserCodeOverflowException(
            UserCodeOverflowException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(QueryException.class)
    public ResponseEntity<Object> handleQueryException(
            QueryException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleQueryException(
            BadCredentialsException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(exception.getMessage());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
