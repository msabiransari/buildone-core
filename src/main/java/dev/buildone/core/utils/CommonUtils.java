package dev.buildone.core.utils;

import dev.buildone.core.config.CoreConfiguration;
import dev.buildone.core.utils.exceptions.ContentProcessingException;
import dev.buildone.core.utils.exceptions.InvalidPathException;
import freemarker.template.Template;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommonUtils {
  public static String removeLastSlash(String path) {
    if (path.endsWith("/")) {
      return path.substring(0, path.length() - 1);
    }
    return path;
  }

  public static String removeFirstSlash(String path) {
    if (path.startsWith("/")) {
      return path.substring(1);
    }
    return path;
  }

  public static freemarker.template.Configuration createTemplateConfiguration() {
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
    cfg.setClassForTemplateLoading(CoreConfiguration.class, "/");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(true);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setFallbackOnNullLoopVariable(false);
    return cfg;
  }

  public static Resource getResource(String path) {
    if(path.startsWith("classpath:")) {
      return new ClassPathResource(path.substring(10));
    } else {
      return new FileSystemResource(path.substring(5));
    }
  }

  public static List<String> loadFrameworkFiles(String libsFolder, String fileType) {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource[] resources;
    try {
      resources = resolver.getResources(String.format("%s/%s/*.%s", libsFolder, fileType, fileType));
      return Arrays.stream(resources).map(Resource::getFilename).toList();
    } catch(Exception e) {
      throw new InvalidPathException(String.format(
          "Path defined in frontend.libs.folder %s not valid. Please make sure there is a folder '%s' inside this path with all the framework %s files inside.",
          libsFolder, fileType, fileType));
    }
  }

  public static String createPageScript(List<String> frameworkFiles,
                                        String fileType,
                                        String libsPath,
                                        freemarker.template.Configuration templateConfiguration) {
    StringWriter writer = new StringWriter();
    if(frameworkFiles.isEmpty()) {
      return "<!-- No framework " + fileType + " files found -->";
    }

    Map<String, Object> model = Map.of("path", libsPath,
        "files", frameworkFiles);
    String templateName = "/templates/index-resources/resource-" + fileType + ".ftl";
    try {
      Template template = templateConfiguration.getTemplate(templateName);
      template.process(model, writer);
      return writer.toString();
    } catch (Exception e) {
      throw new ContentProcessingException("Error while preparing content " + templateName, e);
    }
  }
}