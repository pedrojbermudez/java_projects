/*
 * @author Pedro Javier Bermudez Araguez
 * 
 */
package secretSanta;
public class FriendException extends RuntimeException {
  public FriendException() {
    super();
  }
  
  public FriendException(String msg) {
    super(msg);
  }
}