package internal.management.accounts.config.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class CustomExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(
            ValidationException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY, exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

    @ExceptionHandler(UserCodeOverflowException.class)
    public ResponseEntity<Object> handleUserCodeOverflowException(
            UserCodeOverflowException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

    @ExceptionHandler(QueryException.class)
    public ResponseEntity<Object> handleQueryException(
            QueryException exception, WebRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.convert());
        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }
}
