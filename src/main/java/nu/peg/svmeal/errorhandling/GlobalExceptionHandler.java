package nu.peg.svmeal.errorhandling;

import nu.peg.svmeal.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles all exceptions and creates JSON error pages
 *
 * @author Joel Messerli @03.05.2017
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleAll(Exception ex) {
        Response<Void> response = new Response<>(
                String.format("Internal Server Error: %s", ex.getClass().getCanonicalName())
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
