package nu.peg.svmeal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MealPlanParsingException extends RuntimeException {
  public MealPlanParsingException(String message) {
    super(message);
  }
}
