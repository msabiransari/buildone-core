package dev.buildone.core.domain.model.frontend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrontendContent {
  private String name;
  private String content;
}