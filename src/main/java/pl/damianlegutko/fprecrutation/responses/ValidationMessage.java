package pl.damianlegutko.fprecrutation.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;


public class ValidationMessage {
    private String message;
    private String title;

    static ExceptionMessage createExceptionMessageFromBaseException(ValidationException validationException){
        return ExceptionMessage
                .builder()
                .title(validationException.getClass().getSimpleName())
                .message(validationException.getMessage())
                .build();
    }

    public static ResponseEntity createResponseEntity(ValidationException validationException, HttpStatus httpStatus){
        return new ResponseEntity<>(createExceptionMessageFromBaseException(validationException), httpStatus);
    }

}