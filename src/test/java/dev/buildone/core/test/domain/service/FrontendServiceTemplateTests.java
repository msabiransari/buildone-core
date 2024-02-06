package dev.buildone.core.test.domain.service;

import dev.buildone.core.config.CoreConfiguration;
import dev.buildone.core.domain.service.frontend.FrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {CoreConfiguration.class})
@ActiveProfiles("test")
public class FrontendServiceTemplateTests {

  @Autowired
  private FrontendService frontendService;
}