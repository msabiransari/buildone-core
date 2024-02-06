package dev.buildone.core.test.domain.service;

import dev.buildone.core.domain.model.frontend.AppSettings;
import dev.buildone.core.domain.model.frontend.FrontendComponent;
import dev.buildone.core.domain.model.frontend.FrontendConfigs;
import dev.buildone.core.domain.model.frontend.FrontendContent;
import dev.buildone.core.domain.service.frontend.FrontendService;
import freemarker.template.Configuration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static dev.buildone.core.config.CoreConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractCoreConfigurationTest {

  protected FrontendService frontendService;

  protected FrontendConfigs frontendConfigs;

  protected Configuration templateConfiguration;

  protected AppSettings appSettings;

  protected List<FrontendContent> contents;
  public AbstractCoreConfigurationTest(FrontendService frontendService,
                                       FrontendConfigs frontendConfigs,
                                       Configuration templateConfiguration,
                                       AppSettings appSettings,
                                       List<FrontendContent> contents) {
    this.frontendService = frontendService;
    this.frontendConfigs = frontendConfigs;
    this.templateConfiguration = templateConfiguration;
    this.appSettings = appSettings;
    this.contents = contents;
  }

  protected abstract String getProtocol();

  protected void validate(FrontendConfigs frontendConfigs) {
    assertNotNull(frontendConfigs);
    assertEquals("/static/libs", frontendConfigs.getLibsFolder());
    assertTrue(frontendConfigs.getAppFolder().startsWith(getProtocol()));
    assertEquals("app-settings.json", frontendConfigs.getAppSettingsFileName());
    assertEquals("/components", frontendConfigs.getAppComponentsFolder());
  }

  @Test
  public void whenTemplateIsConfiguredCorrectly_itCreatesABean() {
    assertNotNull(templateConfiguration);
  }

  @Test
  public void whenServiceIsConfiguredCorrectly_itCreatesABean() {
    assertNotNull(frontendService);
  }

  @Test
  public void whenAppSettingsProvided_itLoadsSettingsCorrectly() {
    assertNotNull(appSettings.getVersion());
    assertNotNull(appSettings.getTitle());
    assertNotNull(appSettings.getComponents());
    assertEquals(3, appSettings.getComponents().size());

    Optional<FrontendComponent> appToolbar = appSettings.getComponents().stream().filter(c -> c.getName().equals("app-toolbar")).findFirst();
    assertTrue(appToolbar.isPresent());
    assertEquals("app-toolbar", appToolbar.get().getName());
    assertEquals("/tops", appToolbar.get().getFolder());

    Optional<FrontendComponent> appFooter = appSettings.getComponents().stream().filter(c -> c.getName().equals("app-footer")).findFirst();
    assertTrue(appFooter.isPresent());
    assertEquals("app-footer", appFooter.get().getName());
    assertEquals("/", appFooter.get().getFolder());

    Optional<FrontendComponent> appBody = appSettings.getComponents().stream().filter(c -> c.getName().equals("app-body")).findFirst();
    assertTrue(appBody.isPresent());
    assertEquals("app-body", appBody.get().getName());
    assertEquals("/", appBody.get().getFolder());
  }

  @Test
  public void whenPropertiesAreReadSuccessfully_itCreatesConfigs() {
    validate(frontendConfigs);
  }

  @Test
  public void whenFrameworkCssIsRequested_itReturnsCorrectCss() {
    Optional<FrontendContent> frameworkCss = contents.stream()
        .filter(c -> c.getName().equals("framework_include_css")).findFirst();
    assertTrue(frameworkCss.isPresent());
    assertNotNull(frameworkCss.get().getContent());
    String frameworkCssContent = frameworkCss.get().getContent();
    assertNotNull(frameworkCssContent);
    assertTrue(frameworkCssContent.contains(LIBS_STYLES_PATH + "/vue-test-1.css"));
    assertTrue(frameworkCssContent.contains(LIBS_STYLES_PATH + "/vuetify-test-1.css"));
  }

  @Test
  public void whenFrameworkJsIsRequested_itReturnsCorrectJs() {
    Optional<FrontendContent> frameworkJs = contents.stream()
        .filter(c -> c.getName().equals("framework_include_js")).findFirst();
    assertTrue(frameworkJs.isPresent());
    assertNotNull(frameworkJs.get().getContent());
    String frameworkJsContent = frameworkJs.get().getContent();
    assertNotNull(frameworkJsContent);
    assertTrue(frameworkJsContent.contains(LIBS_SCRIPTS_PATH + "/vue-test-1.js"));
    assertTrue(frameworkJsContent.contains(LIBS_SCRIPTS_PATH + "/vuetify-test-1.js"));
  }

  @Test
  public void whenAppCssIsRequested_itReturnsCorrectCss() {
    Optional<FrontendContent> frameworkCss = contents.stream()
        .filter(c -> c.getName().equals("app_include_css")).findFirst();
    assertTrue(frameworkCss.isPresent());
    assertNotNull(frameworkCss.get().getContent());
    String frameworkCssContent = frameworkCss.get().getContent();
    assertNotNull(frameworkCssContent);
    assertTrue(frameworkCssContent.contains(APP_STYLES_PATH + "/app-toolbar"));
    assertTrue(frameworkCssContent.contains(APP_STYLES_PATH + "/app-body"));
    assertTrue(frameworkCssContent.contains(APP_STYLES_PATH + "/app-footer"));
  }

  @Test
  public void whenAppJsIsRequested_itReturnsCorrectJs() {
    Optional<FrontendContent> appJs = contents.stream()
        .filter(c -> c.getName().equals("app_include_js")).findFirst();
    assertTrue(appJs.isPresent());
    assertNotNull(appJs.get().getContent());
    String appJsContent = appJs.get().getContent();
    assertNotNull(appJsContent);
    assertTrue(appJsContent.contains(APP_SCRIPTS_PATH + "/app-toolbar"));
    assertTrue(appJsContent.contains(APP_SCRIPTS_PATH + "/app-body"));
    assertTrue(appJsContent.contains(APP_SCRIPTS_PATH + "/app-footer"));
  }
}