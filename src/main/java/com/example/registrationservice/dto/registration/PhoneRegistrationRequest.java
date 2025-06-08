package com.example.registrationservice.dto.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRegistrationRequest {
  @NotBlank(message = "{validation.phone.required}")
  @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "{validation.phone.invalid}")
  private String phone;

  @NotBlank(message = "{validation.password.required}")
  @Size(min = 8, message = "{validation.password.short}")
  private String password;
}
