package com.example.registrationservice.dto.registration;

import java.util.UUID;

public record RegistrationResponse(UUID registrationId, String message) {
  public static RegistrationResponse of(UUID registrationId, String message) {
    return new RegistrationResponse(registrationId, message);
  }
}
