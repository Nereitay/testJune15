package es.kiwi.prices.infrastructure.output.exception;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.infrastructure.output.exception.data.responses.ExceptionResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomizedExceptionAdapterTest {

    @Mock
    private WebRequest webRequestMock;

    @InjectMocks
    private CustomizedExceptionAdapter customizedExceptionAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleAllExceptions_InternalServerError() {
        Exception ex = new Exception("Test Exception");
        when(webRequestMock.getDescription(false)).thenReturn("Test Description");

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleAllExceptions(ex, webRequestMock);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals("Test Exception", responseBody.getMessage());
        assertEquals(Collections.singletonList("Test Description"), responseBody.getDetails());
    }

    @Test
    void handleUserNotFoundException_NotFound() {
        PricesNotFoundException ex = new PricesNotFoundException("Prices not found");
        when(webRequestMock.getDescription(false)).thenReturn("Test Description");

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleUserNotFoundException(ex, webRequestMock);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals("Prices not found", responseBody.getMessage());
        assertEquals(Collections.singletonList("Test Description"), responseBody.getDetails());
    }

    @Test
    void constraintViolationExceptionHandler_BadRequest() {
        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", new HashSet<>());
        when(webRequestMock.getDescription(false)).thenReturn("Test Description");

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.ConstraintViolationExceptionHandler(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals("Validation Failed", responseBody.getMessage());
        assertEquals(Collections.emptyList(), responseBody.getDetails()); // Assuming no specific details here
    }

    @Test
    void handleBindException_BadRequest() {
        BindException ex = new BindException(new Object(), "objectName");
        ex.addError(new org.springframework.validation.ObjectError("objectName", "Test Error"));

        when(webRequestMock.getDescription(false)).thenReturn("Test Description");

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleBindException(ex, null, HttpStatus.BAD_REQUEST, webRequestMock);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals("Validation Failed", responseBody.getMessage());

        assertTrue(responseBody.getDetails().get(0).contains("Test Error"));
    }

    @Test
    void handleMethodArgumentNotValid_BadRequest() throws NoSuchMethodException {
        Object target = null;

        BindingResult bindingResultMock = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResultMock.getAllErrors()).thenReturn(Collections.singletonList(new org.springframework.validation.FieldError("objectName", "fieldName", "Test Error")));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(Object.class.getDeclaredConstructor(), -1), bindingResultMock);

        when(webRequestMock.getDescription(false)).thenReturn("Test Description");

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleMethodArgumentNotValid(ex, null, HttpStatus.BAD_REQUEST, webRequestMock);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals("Validation Failed", responseBody.getMessage());
        assertEquals(Collections.singletonList("Test Error"), responseBody.getDetails());
    }
}