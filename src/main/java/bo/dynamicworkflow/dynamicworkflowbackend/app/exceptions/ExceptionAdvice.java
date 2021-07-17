package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions;

import bo.dynamicworkflow.dynamicworkflowbackend.app.services.dto.responses.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    GeneralResponse response;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> exception(Exception exception) {
        exception.printStackTrace();
        response = new GeneralResponse(false, null, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}