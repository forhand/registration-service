package com.example.registrationservice.handler.exception;

public class VerificationAlreadyCompletedException extends RegistrationException {
  public VerificationAlreadyCompletedException(String message) {
    super(message);
  }
}
