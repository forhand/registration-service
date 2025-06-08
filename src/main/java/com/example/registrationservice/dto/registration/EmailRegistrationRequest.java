package com.example.registrationservice.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRegistrationRequest {
  @NotBlank(message = "{validation.email.required}")
  @Email(message = "{validation.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.password.required}")
  private String password;
}
