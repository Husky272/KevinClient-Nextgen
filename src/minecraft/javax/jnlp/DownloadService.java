

package javax.jnlp;

public interface DownloadService {

  boolean isResourceCached(java.net.URL ref, java.lang.String version);
  boolean isPartCached(java.lang.String part);
  boolean isPartCached(java.lang.String[] parts);
  boolean isExtensionPartCached(java.net.URL ref, java.lang.String version, java.lang.String part);
  boolean isExtensionPartCached(java.net.URL ref, java.lang.String version, java.lang.String[] parts);
  void loadResource(java.net.URL ref, java.lang.String version, DownloadServiceListener progress) throws java.io.IOException;
  void loadPart(java.lang.String part, DownloadServiceListener progress) throws java.io.IOException;
  void loadPart(java.lang.String[] parts, DownloadServiceListener progress) throws java.io.IOException;
  void loadExtensionPart(java.net.URL ref, java.lang.String version, java.lang.String part, DownloadServiceListener progress) throws java.io.IOException;
  void loadExtensionPart(java.net.URL ref, java.lang.String version, java.lang.String[] parts, DownloadServiceListener progress) throws java.io.IOException;
  void removeResource(java.net.URL ref, java.lang.String version) throws java.io.IOException;
  void removePart(java.lang.String part) throws java.io.IOException;
  void removePart(java.lang.String[] parts) throws java.io.IOException;
  void removeExtensionPart(java.net.URL ref, java.lang.String version, java.lang.String part) throws java.io.IOException;
  void removeExtensionPart(java.net.URL ref, java.lang.String version, java.lang.String[] parts) throws java.io.IOException;
  DownloadServiceListener getDefaultProgressWindow();

}

