package com.example.registrationservice.handler.exception;

public class InvalidVerificationCodeException extends RegistrationException {
  public InvalidVerificationCodeException(String message) {
    super(message);
  }
}
