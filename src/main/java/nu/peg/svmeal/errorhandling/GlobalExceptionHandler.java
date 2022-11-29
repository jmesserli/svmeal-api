package nu.peg.svmeal.errorhandling;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import nu.peg.svmeal.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CallNotPermittedException.class)
  public ResponseEntity<Response<?>> handleOpenCircuit(CallNotPermittedException e) {
    String circuitBreaker = e.getCausingCircuitBreakerName();
    LOGGER.warn("Error caught: Circuit breaker {} is currently open.", circuitBreaker);

    Response<Object> response =
        new Response<>(
            String.format(
                "The circuit breaker for backend %s is currently open because of repeated failures. Please try again later.",
                circuitBreaker));
    return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response<?>> handleAll(Exception ex) {
    LOGGER.warn("Handling exception in global exception handler", ex);

    Response<Void> response =
        new Response<>(
            String.format("Internal Server Error: %s", ex.getClass().getCanonicalName()));
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
