package com.example.registrationservice.handler.exception;

public class VerificationExpiredException extends RegistrationException {
  public VerificationExpiredException(String message) {
    super(message);
  }
}
