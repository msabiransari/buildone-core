package dev.buildone.core.test.domain.service;

import dev.buildone.core.config.CoreConfiguration;
import dev.buildone.core.domain.model.frontend.AppSettings;
import dev.buildone.core.domain.model.frontend.FrontendConfigs;
import dev.buildone.core.domain.model.frontend.FrontendContent;
import dev.buildone.core.domain.service.frontend.FrontendService;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = {CoreConfiguration.class})
@ActiveProfiles("classpath")
public class ClasspathCoreConfigurationTest extends AbstractCoreConfigurationTest {
  public ClasspathCoreConfigurationTest(@Autowired FrontendService frontendService,
                                        @Autowired FrontendConfigs frontendConfigs,
                                        @Autowired Configuration templateConfiguration,
                                        @Autowired AppSettings appSettings,
                                        @Autowired List<FrontendContent> contents) {
    super(frontendService, frontendConfigs, templateConfiguration, appSettings, contents);
  }

  @Override
  public String getProtocol() {
    return "classpath";
  }
}