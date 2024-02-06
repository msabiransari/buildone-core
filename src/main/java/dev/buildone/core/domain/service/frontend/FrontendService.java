package dev.buildone.core.domain.service.frontend;

import dev.buildone.core.domain.model.frontend.AppSettings;
import dev.buildone.core.domain.model.frontend.FrontendConfigs;
import dev.buildone.core.domain.model.frontend.FrontendContent;
import freemarker.template.Configuration;

import java.util.List;

public class FrontendService {
  //private final freemarker.template.Configuration templateConfiguration;
  private final FrontendConfigs frontendConfigs;

  private final AppSettings appSettings;

  private final List<FrontendContent> contents;

  public FrontendService(FrontendConfigs frontendConfigs,
                         AppSettings appSettings,
                         List<FrontendContent> contents) {
    //this.templateConfiguration = templateConfiguration;
    this.frontendConfigs = frontendConfigs;
    this.appSettings = appSettings;
    this.contents = contents;
  }


}