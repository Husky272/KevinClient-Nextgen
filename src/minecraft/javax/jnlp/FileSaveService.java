

package javax.jnlp;

public interface FileSaveService {

  FileContents saveFileDialog(java.lang.String pathHint, java.lang.String[] extensions, java.io.InputStream stream, java.lang.String name) throws java.io.IOException;
  FileContents saveAsFileDialog(java.lang.String pathHint, java.lang.String[] extensions, FileContents contents) throws java.io.IOException;

}

