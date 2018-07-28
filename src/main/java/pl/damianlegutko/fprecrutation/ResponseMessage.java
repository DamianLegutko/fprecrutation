package pl.damianlegutko.fprecrutation;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class ResponseMessage {
    private String message;
    private int errorCode;
    private String title;

    public static ResponseMessage createResponseMessageFromBaseException(BaseException baseException){
        return ResponseMessage
                .builder()
                .title(baseException.getClass().getSimpleName())
                .errorCode(baseException.getErrorCode().ordinal())
                .message(baseException.getErrorMessage())
                .build();
    }

    public static ResponseEntity<Object> createResponseEntity(BaseException baseException, HttpStatus httpStatus){
        return new ResponseEntity<>(createResponseMessageFromBaseException(baseException), httpStatus);
    }
}