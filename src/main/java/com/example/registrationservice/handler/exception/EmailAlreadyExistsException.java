package com.example.registrationservice.handler.exception;

public class EmailAlreadyExistsException extends RegistrationException {
  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
