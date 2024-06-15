package es.kiwi.prices.infrastructure.output.exception;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.infrastructure.output.exception.data.responses.ExceptionResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@ControllerAdvice
public class CustomizedExceptionAdapter extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponses exceptionResponses = new ExceptionResponses(LocalDateTime.now(), ex.getMessage(), Arrays.asList(request.getDescription(false)));

        return new ResponseEntity(exceptionResponses, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PricesNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(PricesNotFoundException ex, WebRequest request) {
        ExceptionResponses exceptionResponses = new ExceptionResponses(LocalDateTime.now(), ex.getMessage(), Arrays.asList(request.getDescription(false)));

        return new ResponseEntity(exceptionResponses, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(error -> {
            errors.add(error.toString());
        });

        ExceptionResponses exceptionResponses = new ExceptionResponses(LocalDateTime.now(), "Validation Failed", errors);

        return new ResponseEntity(exceptionResponses, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(error.toString());
        });

        ExceptionResponses exceptionResponses = new ExceptionResponses(LocalDateTime.now(), "Validation Failed", errors);

        return new ResponseEntity(exceptionResponses, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().stream().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });

        ExceptionResponses exceptionResponses = new ExceptionResponses(LocalDateTime.now(), "Validation Failed", errors);

        return new ResponseEntity(exceptionResponses, HttpStatus.BAD_REQUEST);
    }
}
