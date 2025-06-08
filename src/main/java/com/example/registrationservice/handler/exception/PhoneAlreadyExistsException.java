package com.example.registrationservice.handler.exception;

public class PhoneAlreadyExistsException extends RegistrationException {
  public PhoneAlreadyExistsException(String message) {
    super(message);
  }
}
