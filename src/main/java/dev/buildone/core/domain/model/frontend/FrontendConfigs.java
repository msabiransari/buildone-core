package dev.buildone.core.domain.model.frontend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontendConfigs {
  @Builder.Default
  private String libsFolder = "/static/libs";
  @Builder.Default
  private String appFolder = "/frontend";
  @Builder.Default
  private String appSettingsFileName = "app-settings.json";
  @Builder.Default
  private String appComponentsFolder = "/components";
}