package com.example.registrationservice.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.registrationservice.dto.registration.EmailRegistrationRequest;
import com.example.registrationservice.dto.registration.PhoneRegistrationRequest;
import com.example.registrationservice.dto.registration.RegistrationResponse;
import com.example.registrationservice.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping("/email")
  public ResponseEntity<RegistrationResponse> registerByEmail(
      @Valid @RequestBody EmailRegistrationRequest request) {
    return ResponseEntity.ok(registrationService.registerByEmail(request));
  }

  @PostMapping("/phone")
  public ResponseEntity<RegistrationResponse> registerByPhone(
      @Valid @RequestBody PhoneRegistrationRequest request) {
    return ResponseEntity.ok(registrationService.registerByPhone(request));
  }
}
