package dev.buildone.core.domain.service.frontend;

import dev.buildone.core.domain.model.frontend.FrontendComponent;
import dev.buildone.core.domain.model.frontend.FrontendConfigs;
import dev.buildone.core.domain.model.frontend.FrontendContent;
import dev.buildone.core.utils.exceptions.ContentProcessingException;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static dev.buildone.core.utils.CommonUtils.getResource;

@Slf4j
public class ContentGenerator {
  private final freemarker.template.Configuration templateConfiguration;

  public ContentGenerator(freemarker.template.Configuration templateConfiguration) {
    this.templateConfiguration = templateConfiguration;
  }

  public FrontendContent generateComponentScript(FrontendConfigs configs, FrontendComponent component) {
    String fileName = configs.getAppFolder() + component.getFolder() + component.getName() + "/index.js";
    String content = readContent(fileName);
    try {
      Template template = new Template(component.getName(), new StringReader(content), templateConfiguration);
      StringWriter writer = new StringWriter();
      template.process(null, writer);
      return new FrontendContent(component.getName(), writer.toString());
    } catch(Exception e) {
      throw new ContentProcessingException("Error while processing framework " + fileName, e);
    }
  }

  private String readContent(String path) {
    Resource resource = getResource(path);
    String content = "";
    try {
      content = resource.exists() ? resource.getContentAsString(StandardCharsets.UTF_8) : "";
    } catch (Exception e) {
      log.error("Error reading file: {}", path, e);
      content = "/* error reading file: " + path + " */";
    }
    return content;
  }
}