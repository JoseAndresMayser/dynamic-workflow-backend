package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseException {

    GeneralResponse response;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> exception(Exception e) {
        response = new GeneralResponse(false, null, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}