

package javax.jnlp;

public interface ExtensionInstallerService {

  java.lang.String getInstallPath();
  java.lang.String getExtensionVersion();
  java.net.URL getExtensionLocation();
  void hideProgressBar();
  void hideStatusWindow();
  void setHeading(java.lang.String heading);
  void setStatus(java.lang.String status);
  void updateProgress(int value);
  void installSucceeded(boolean needsReboot);
  void installFailed();
  void setJREInfo(java.lang.String platformVersion, java.lang.String jrePath);
  void setNativeLibraryInfo(java.lang.String path);
  java.lang.String getInstalledJRE(java.net.URL url, java.lang.String version);

}

