package ru.testwork.UserManager.web;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.testwork.UserManager.util.DoubleException;
import ru.testwork.UserManager.util.NotFoundException;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionInfoHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public JSONObject handleValidationExceptions(MethodArgumentNotValidException ex) {
    JSONObject errors = new JSONObject();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    JSONObject entity = new JSONObject();
    entity.put("success", "false");
    entity.put("errors", errors);
    return entity;
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({DoubleException.class})
  public JSONObject handleValidationExceptions(DoubleException ex) {
    JSONObject errors = new JSONObject();
    errors.put("Conflict", ex.getMessage());
    return errors;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({NotFoundException.class})
  public JSONObject handleValidationExceptions(NotFoundException ex) {
    JSONObject errors = new JSONObject();
    errors.put("Not found", ex.getMessage());
    return errors;
  }
}
