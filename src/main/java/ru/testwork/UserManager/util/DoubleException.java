package ru.testwork.UserManager.util;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Double login")
public class DoubleException extends RuntimeException {

  public DoubleException(String msg) {
    super(msg);
  }
}