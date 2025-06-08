package com.example.registrationservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.registrationservice.dto.registration.EmailRegistrationRequest;
import com.example.registrationservice.dto.registration.PhoneRegistrationRequest;
import com.example.registrationservice.dto.registration.RegistrationResponse;
import com.example.registrationservice.repository.RegistrationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
  private final RegistrationRepository registrationRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public RegistrationResponse registerByEmail(EmailRegistrationRequest request) {

    return null;
  }

  @Transactional
  public RegistrationResponse registerByPhone(PhoneRegistrationRequest request) {
    return null;
  }
}
