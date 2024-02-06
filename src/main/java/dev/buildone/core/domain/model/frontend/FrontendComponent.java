package dev.buildone.core.domain.model.frontend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrontendComponent {
  @NonNull
  private String name;
  @Builder.Default
  private String folder = "/";
}