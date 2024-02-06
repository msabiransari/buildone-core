package dev.buildone.core.utils.exceptions;

public class InvalidAppSettingsException extends RuntimeException {
  public InvalidAppSettingsException(String appFolder, String appSettingsFileName) {
    super(createErrorMessage(appFolder, appSettingsFileName));
  }

  private static String createErrorMessage(String appFolder, String appSettingsFileName) {
    return "Error reading app settings defined in frontend.app.settings = " + appSettingsFileName + " and frontend.app.folder = " + appFolder;
  }
}
