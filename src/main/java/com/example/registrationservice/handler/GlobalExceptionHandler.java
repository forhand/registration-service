package com.example.registrationservice.handler;

import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.registrationservice.dto.error.ErrorResponse;
import com.example.registrationservice.dto.error.ValidationError;
import com.example.registrationservice.handler.exception.EmailAlreadyExistsException;
import com.example.registrationservice.handler.exception.PhoneAlreadyExistsException;
import com.example.registrationservice.handler.exception.RegistrationException;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<ValidationError> details =
        ex.getBindingResult().getFieldErrors().stream().map(this::mapFieldError).toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorResponse(
                messageSource.getMessage(
                    "error.validation.failed", null, LocaleContextHolder.getLocale()),
                details));
  }

  @ExceptionHandler({EmailAlreadyExistsException.class, PhoneAlreadyExistsException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorResponse> handleAlreadyExistsException(RegistrationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponse(
                messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale()),
                HttpStatus.INTERNAL_SERVER_ERROR.toString()));
  }

  private ValidationError mapFieldError(FieldError error) {
    String messageKey = error.getDefaultMessage();
    if (messageKey != null && messageKey.startsWith("{") && messageKey.endsWith("}")) {
      messageKey = messageKey.substring(1, messageKey.length() - 1);
    }

    Object[] arguments = error.getArguments();
    if (error.getCode() != null && error.getCode().equals("Size")) {
      // For @Size annotation, we need to use min value as the argument
      arguments = new Object[] {error.getArguments()[2]}; // index 2 contains the min value
    }

    String message =
        messageSource.getMessage(
            messageKey,
            arguments,
            messageKey, // fallback to the key itself if not found
            LocaleContextHolder.getLocale());
    return new ValidationError(error.getField(), message);
  }
}
