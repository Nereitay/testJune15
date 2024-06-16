package es.kiwi.prices.infrastructure.output.exception;

import es.kiwi.prices.domain.exception.PricesNotFoundException;
import es.kiwi.prices.infrastructure.output.constants.ExceptionConstants;
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

    private static final String TEST_DESCRIPTION = "Test Description";
    private static final String TEST_EXCEPTION = "Test Exception";
    private static final String NOT_FOUND = "Prices not found";
    private static final String TEST_ERROR = "Test Error";

    @Test
    void handleAllExceptions_InternalServerError() {
        Exception ex = new Exception(TEST_EXCEPTION);
        when(webRequestMock.getDescription(false)).thenReturn(TEST_DESCRIPTION);

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleAllExceptions(ex, webRequestMock);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals(TEST_EXCEPTION, responseBody.getMessage());
        assertEquals(Collections.singletonList(TEST_DESCRIPTION), responseBody.getDetails());
    }

    @Test
    void handleUserNotFoundException_NotFound() {
        PricesNotFoundException ex = new PricesNotFoundException(NOT_FOUND);
        when(webRequestMock.getDescription(false)).thenReturn(TEST_DESCRIPTION);

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleUserNotFoundException(ex, webRequestMock);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals(NOT_FOUND, responseBody.getMessage());
        assertEquals(Collections.singletonList(TEST_DESCRIPTION), responseBody.getDetails());
    }

    @Test
    void constraintViolationExceptionHandler_BadRequest() {
        ConstraintViolationException ex = new ConstraintViolationException(ExceptionConstants.VALIDATION_EXCEPTION, new HashSet<>());
        when(webRequestMock.getDescription(false)).thenReturn(TEST_DESCRIPTION);

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.ConstraintViolationExceptionHandler(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals(ExceptionConstants.VALIDATION_EXCEPTION, responseBody.getMessage());
        assertEquals(Collections.emptyList(), responseBody.getDetails());
    }

    @Test
    void handleBindException_BadRequest() {
        BindException ex = new BindException(new Object(), "objectName");
        ex.addError(new org.springframework.validation.ObjectError("objectName", TEST_ERROR));

        when(webRequestMock.getDescription(false)).thenReturn(TEST_DESCRIPTION);

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleBindException(ex, null, HttpStatus.BAD_REQUEST, webRequestMock);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals(ExceptionConstants.VALIDATION_EXCEPTION, responseBody.getMessage());

        assertTrue(responseBody.getDetails().get(0).contains(TEST_ERROR));
    }

    @Test
    void handleMethodArgumentNotValid_BadRequest() throws NoSuchMethodException {

        BindingResult bindingResultMock = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResultMock.getAllErrors()).thenReturn(Collections.singletonList(new org.springframework.validation.FieldError("objectName", "fieldName", TEST_ERROR)));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(Object.class.getDeclaredConstructor(), -1), bindingResultMock);

        when(webRequestMock.getDescription(false)).thenReturn(TEST_DESCRIPTION);

        ResponseEntity<Object> responseEntity = customizedExceptionAdapter.handleMethodArgumentNotValid(ex, null, HttpStatus.BAD_REQUEST, webRequestMock);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ExceptionResponses responseBody = (ExceptionResponses) responseEntity.getBody();
        assertEquals(ExceptionConstants.VALIDATION_EXCEPTION, responseBody.getMessage());
        assertEquals(Collections.singletonList(TEST_ERROR), responseBody.getDetails());
    }
}