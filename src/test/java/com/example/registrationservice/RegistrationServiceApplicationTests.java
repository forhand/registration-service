package com.example.registrationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.registrationservice.config.BaseKafkaTest;

@SpringBootTest
@ActiveProfiles("test")
class RegistrationServiceApplicationTests extends BaseKafkaTest {

  @Test
  void contextLoads() {}
}
