package com.example.registrationservice.handler.exception;

/** Base exception class for all registration-related exceptions */
public abstract class RegistrationException extends RuntimeException {
  public RegistrationException(String message) {
    super(message);
  }
}
