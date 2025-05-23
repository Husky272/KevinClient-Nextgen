

package javax.jnlp;

public interface FileOpenService {

  FileContents openFileDialog(java.lang.String pathHint, java.lang.String[] extensions) throws java.io.IOException;
  FileContents[] openMultiFileDialog(java.lang.String pathHint, java.lang.String[] extensions) throws java.io.IOException;

}

