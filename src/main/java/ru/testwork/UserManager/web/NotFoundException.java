package ru.testwork.UserManager.web;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not found")
public class NotFoundException extends RuntimeException {

  public NotFoundException(String msg) {
    super(msg);
  }
}