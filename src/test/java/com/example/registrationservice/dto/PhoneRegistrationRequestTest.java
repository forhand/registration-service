package com.example.registrationservice.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.registrationservice.dto.registration.PhoneRegistrationRequest;

class PhoneRegistrationRequestTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldValidateValidRequest() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest("+79001234567", "password123");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "abc", "+", "+a", "+0"})
  void shouldNotValidateInvalidPhone(String phone) {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest(phone, "password123");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("phone");
  }

  @Test
  void shouldNotValidateEmptyPhone() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest("", "password123");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(2);
    assertThat(violations).allMatch(v -> v.getPropertyPath().toString().equals("phone"));
  }

  @Test
  void shouldNotValidateBlankPhone() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest(" ", "password123");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(2);
    assertThat(violations).allMatch(v -> v.getPropertyPath().toString().equals("phone"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"123", "pass"})
  void shouldNotValidateShortPassword(String password) {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest("+79001234567", password);

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("password");
  }

  @Test
  void shouldNotValidateEmptyPassword() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest("+79001234567", "");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(2);
    assertThat(violations).allMatch(v -> v.getPropertyPath().toString().equals("password"));
  }

  @Test
  void shouldNotValidateBlankPassword() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest("+79001234567", " ");

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(2);
    assertThat(violations).allMatch(v -> v.getPropertyPath().toString().equals("password"));
  }

  @Test
  void shouldNotValidateNullFields() {
    // given
    PhoneRegistrationRequest request = new PhoneRegistrationRequest(null, null);

    // when
    Set<ConstraintViolation<PhoneRegistrationRequest>> violations = validator.validate(request);

    // then
    assertThat(violations).hasSize(2);
  }
}
