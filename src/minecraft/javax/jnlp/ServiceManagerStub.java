

package javax.jnlp;

public interface ServiceManagerStub {

  java.lang.Object lookup(java.lang.String name) throws UnavailableServiceException;
  java.lang.String[] getServiceNames();

}

