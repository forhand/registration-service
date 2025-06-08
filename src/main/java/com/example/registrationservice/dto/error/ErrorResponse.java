package com.example.registrationservice.dto.error;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String message, String code, LocalDateTime timestamp, List<ValidationError> details) {

  public ErrorResponse {
    // Defensive copying of mutable fields
    details = details != null ? Collections.unmodifiableList(new ArrayList<>(details)) : null;
  }

  public ErrorResponse(String message) {
    this(message, null, LocalDateTime.now(), null);
  }

  public ErrorResponse(String message, String code) {
    this(message, code, LocalDateTime.now(), null);
  }

  public ErrorResponse(String message, List<ValidationError> details) {
    this(message, null, LocalDateTime.now(), details);
  }
}
