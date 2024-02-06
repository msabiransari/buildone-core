package dev.buildone.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.buildone.core.domain.model.frontend.AppSettings;
import dev.buildone.core.domain.model.frontend.FrontendComponent;
import dev.buildone.core.domain.model.frontend.FrontendConfigs;
import dev.buildone.core.domain.model.frontend.FrontendContent;
import dev.buildone.core.domain.service.frontend.FrontendService;
import dev.buildone.core.utils.exceptions.InvalidAppSettingsException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

import static dev.buildone.core.utils.CommonUtils.*;

@Configuration
public class CoreConfiguration {
  public static final String LIBS_SCRIPTS_PATH = "/libs/scripts/js";
  public static final String LIBS_STYLES_PATH = "/libs/styles/css";
  public static final String APP_SCRIPTS_PATH = "/app/scripts/js";
  public static final String APP_STYLES_PATH = "/app/styles/css";

  @Bean("templateConfiguration")
  public freemarker.template.Configuration templateConfiguration() {
    return createTemplateConfiguration();
  }

  @Bean
  public FrontendConfigs frontendConfigs(@Value("${frontend.libs.folder:#{null}}") String libsFolder,
                                         @Value("${frontend.app.folder:#{null}}") String appFolder,
                                         @Value("${frontend.app.settings:#{null}}") String appSettingsFileName,
                                         @Value("${frontend.app.components.folder:#{null}}") String appComponentsFolder) {
    FrontendConfigs configs = new FrontendConfigs();
    if(libsFolder != null) {
      configs.setLibsFolder(removeLastSlash(libsFolder));
    }
    if(appFolder != null) {
      if(!appFolder.startsWith("classpath:") && !appFolder.startsWith("file:")) {
        appFolder = "classpath:" + removeLastSlash(appFolder);
      }
      configs.setAppFolder(appFolder);
    }
    if(appSettingsFileName != null) {
      configs.setAppSettingsFileName(removeFirstSlash(appSettingsFileName));
    }
    if(appComponentsFolder != null) {
      configs.setAppComponentsFolder(appComponentsFolder);
    }
    return configs;
  }

  @Bean
  public AppSettings appSettings(FrontendConfigs frontendConfigs) {
    ObjectMapper mapper = new ObjectMapper();
    Resource resource = getResource(
        frontendConfigs.getAppFolder() +
            "/" +
            frontendConfigs.getAppSettingsFileName()
    );

    if(!resource.exists()) {
      throw new InvalidAppSettingsException(frontendConfigs.getAppFolder(), frontendConfigs.getAppSettingsFileName());
    }

    try {
      return mapper.readValue(resource.getInputStream(), AppSettings.class);
    } catch (Exception e) {
      throw new InvalidAppSettingsException(frontendConfigs.getAppFolder(), frontendConfigs.getAppSettingsFileName());
    }
  }

  @Bean("frameworkCssContent")
  public FrontendContent frameworkCssContent(FrontendConfigs frontendConfigs,
                                            @Qualifier("templateConfiguration") freemarker.template.Configuration templateConfiguration) {
    List<String> files = loadFrameworkFiles(frontendConfigs.getLibsFolder(), "css");
    String content = createPageScript(files, "css", LIBS_STYLES_PATH, templateConfiguration);
    return FrontendContent.builder()
        .name("framework_include_css")
        .content(content)
        .build();
  }

  @Bean("frameworkJsContent")
  public FrontendContent frameworkJsContent(FrontendConfigs frontendConfigs,
                                            @Qualifier("templateConfiguration") freemarker.template.Configuration templateConfiguration) {
    List<String> files = loadFrameworkFiles(frontendConfigs.getLibsFolder(), "js");
    String content = createPageScript(files, "js", LIBS_SCRIPTS_PATH, templateConfiguration);
    return FrontendContent.builder()
        .name("framework_include_js")
        .content(content)
        .build();
  }

  @Bean("appCssContent")
  public FrontendContent appCssContent(AppSettings appSettings,
                                       @Qualifier("templateConfiguration") freemarker.template.Configuration templateConfiguration) {
    List<String> files = appSettings.getComponents().stream().map(FrontendComponent::getName).toList();
    String componentContents = createPageScript(files, "css", APP_STYLES_PATH, templateConfiguration);

    return FrontendContent.builder()
        .name("app_include_css")
        .content(componentContents)
        .build();
  }

  @Bean("appJsContent")
  public FrontendContent appJsContent(AppSettings appSettings,
                                      @Qualifier("templateConfiguration") freemarker.template.Configuration templateConfiguration) {
    List<String> files = appSettings.getComponents().stream().map(FrontendComponent::getName).toList();
    String componentContents = createPageScript(files, "js", APP_SCRIPTS_PATH, templateConfiguration);
    return FrontendContent.builder()
        .name("app_include_js")
        .content(componentContents)
        .build();
  }

  @Bean
  public FrontendService frontendService(FrontendConfigs frontendConfigs,
                                         AppSettings appSettings,
                                         List<FrontendContent> contents) {
    return new FrontendService(
        frontendConfigs,
        appSettings,
        contents);
  }
}