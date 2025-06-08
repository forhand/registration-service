package com.example.registrationservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.registrationservice.dto.registration.EmailRegistrationRequest;
import com.example.registrationservice.dto.registration.PhoneRegistrationRequest;
import com.example.registrationservice.dto.registration.RegistrationResponse;
import com.example.registrationservice.handler.GlobalExceptionHandler;
import com.example.registrationservice.handler.exception.EmailAlreadyExistsException;
import com.example.registrationservice.handler.exception.PhoneAlreadyExistsException;
import com.example.registrationservice.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

  private static MessageSource messageSource;

  @Mock private RegistrationService registrationService;

  @InjectMocks private RegistrationController registrationController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private UUID registrationId;
  private PhoneRegistrationRequest phoneRegistrationRequest;

  @BeforeAll
  static void setUpClass() {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasename("classpath:messages");
    ms.setDefaultEncoding("UTF-8");
    ms.setUseCodeAsDefaultMessage(true);
    messageSource = ms;
  }

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(registrationController)
            .setControllerAdvice(new GlobalExceptionHandler(messageSource))
            .build();
    objectMapper = new ObjectMapper();
    registrationId = UUID.randomUUID();
    phoneRegistrationRequest = new PhoneRegistrationRequest("+79001234567", "password123");
  }

  @Nested
  @DisplayName("Registration by Email Tests")
  class RegistrationByEmailTests {

    @Test
    void shouldInitiateEmailRegistration() throws Exception {
      // given
      EmailRegistrationRequest request =
          new EmailRegistrationRequest("test@example.com", "password123");

      String successMessage =
          messageSource.getMessage("registration.success.email", null, Locale.getDefault());

      RegistrationResponse response = new RegistrationResponse(registrationId, successMessage);

      when(registrationService.registerByEmail(any(EmailRegistrationRequest.class)))
          .thenReturn(response);

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/email")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value(successMessage))
          .andExpect(jsonPath("$.registrationId").value(registrationId.toString()));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
      // given
      EmailRegistrationRequest request =
          new EmailRegistrationRequest("invalid-email", "password123");

      // when
      ResultActions result =
          mockMvc
              .perform(
                  post("/api/v1/registration/email")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andDo(print());

      // then
      result
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"))
          .andExpect(jsonPath("$.details[0].field").value("email"))
          .andExpect(
              jsonPath("$.details[0].message")
                  .value(
                      messageSource.getMessage(
                          "validation.email.invalid", null, Locale.getDefault())));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsEmpty() throws Exception {
      // given
      EmailRegistrationRequest request = new EmailRegistrationRequest("", "password123");
      String errorMessage =
          messageSource.getMessage("error.validation.failed", null, Locale.getDefault());

      // when
      ResultActions result =
          mockMvc
              .perform(
                  post("/api/v1/registration/email")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(request)))
              .andDo(print());

      // then
      result
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(errorMessage))
          .andExpect(jsonPath("$.details[0].field").value("email"))
          .andExpect(
              jsonPath("$.details[0].message")
                  .value(
                      messageSource.getMessage(
                          "validation.email.required", null, Locale.getDefault())));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
      // given
      EmailRegistrationRequest request =
          new EmailRegistrationRequest("existing@test.com", "password123");

      String errorMessage =
          messageSource.getMessage("registration.email.exists", null, Locale.getDefault());
      doThrow(new EmailAlreadyExistsException(errorMessage))
          .when(registrationService)
          .registerByEmail(any(EmailRegistrationRequest.class));

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/email")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.message").value(errorMessage));
    }
  }

  @Nested
  @DisplayName("Registration by Phone Tests")
  class RegistrationByPhoneTests {

    @Test
    void shouldInitiatePhoneRegistration() throws Exception {
      // given
      PhoneRegistrationRequest request =
          new PhoneRegistrationRequest("+79001234567", "password123");

      String successMessage =
          messageSource.getMessage("registration.success.phone", null, Locale.getDefault());

      RegistrationResponse response = new RegistrationResponse(registrationId, successMessage);

      when(registrationService.registerByPhone(any(PhoneRegistrationRequest.class)))
          .thenReturn(response);

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value(successMessage))
          .andExpect(jsonPath("$.registrationId").value(registrationId.toString()));
    }

    @Test
    void shouldReturnBadRequestWhenPhoneIsInvalid() throws Exception {
      // given
      PhoneRegistrationRequest request = new PhoneRegistrationRequest("123", "password123");
      String errorMessage =
          messageSource.getMessage("error.validation.failed", null, Locale.getDefault());

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(errorMessage))
          .andExpect(
              jsonPath(
                      "$.details[?(@.field == 'phone' && @.message == '"
                          + messageSource.getMessage(
                              "validation.phone.invalid", null, Locale.getDefault())
                          + "')]")
                  .exists());
    }

    @Test
    void shouldReturnBadRequestWhenPhoneIsEmpty() throws Exception {
      // given
      PhoneRegistrationRequest request = new PhoneRegistrationRequest("", "password123");
      String errorMessage =
          messageSource.getMessage("error.validation.failed", null, Locale.getDefault());

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(errorMessage))
          .andExpect(
              jsonPath(
                      "$.details[?(@.field == 'phone' && @.message == '"
                          + messageSource.getMessage(
                              "validation.phone.required", null, Locale.getDefault())
                          + "')]")
                  .exists());
    }

    @Test
    void shouldReturnConflictWhenPhoneAlreadyExists() throws Exception {
      // given
      PhoneRegistrationRequest request =
          new PhoneRegistrationRequest("+79001234567", "password123");

      String errorMessage =
          messageSource.getMessage("registration.phone.exists", null, Locale.getDefault());
      doThrow(new PhoneAlreadyExistsException(errorMessage))
          .when(registrationService)
          .registerByPhone(any(PhoneRegistrationRequest.class));

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
      // given
      PhoneRegistrationRequest request = new PhoneRegistrationRequest("+79001234567", "123");
      String errorMessage =
          messageSource.getMessage("error.validation.failed", null, Locale.getDefault());

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(errorMessage))
          .andExpect(
              jsonPath(
                      "$.details[?(@.field == 'password' && @.message == '"
                          + messageSource.getMessage(
                              "validation.password.short", new Object[] {8}, Locale.getDefault())
                          + "')]")
                  .exists());
    }
  }

  @Nested
  @DisplayName("Other Exceptions Tests")
  class OtherExceptionsTests {

    @Test
    void shouldReturnIllegalArgumentException() throws Exception {
      // given
      String errorMessage = "Invalid argument";
      doThrow(new IllegalArgumentException(errorMessage))
          .when(registrationService)
          .registerByPhone(any(PhoneRegistrationRequest.class));

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(phoneRegistrationRequest)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedException() throws Exception {
      // given
      String expectedMessage =
          messageSource.getMessage("error.unexpected", null, Locale.getDefault());
      doThrow(new RuntimeException("Some internal error"))
          .when(registrationService)
          .registerByPhone(any(PhoneRegistrationRequest.class));

      // when/then
      mockMvc
          .perform(
              post("/api/v1/registration/phone")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(phoneRegistrationRequest)))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").value(expectedMessage))
          .andExpect(jsonPath("$.code").value("500 INTERNAL_SERVER_ERROR"));
    }
  }
}
