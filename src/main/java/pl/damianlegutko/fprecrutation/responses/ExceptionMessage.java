package pl.damianlegutko.fprecrutation.responses;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.damianlegutko.fprecrutation.commonExceptions.BaseException;

@Builder
@Getter
public class ExceptionMessage {
    private String message;
    private int errorCode;
    private String title;

    static ExceptionMessage createExceptionMessageFromBaseException(BaseException baseException){
        return ExceptionMessage
                .builder()
                .title(baseException.getClass().getSimpleName())
                .errorCode(baseException.getErrorCode().ordinal())
                .message(baseException.getErrorMessage())
                .build();
    }

    public static ResponseEntity createResponseEntity(BaseException baseException, HttpStatus httpStatus){
        return new ResponseEntity<>(createExceptionMessageFromBaseException(baseException), httpStatus);
    }
}