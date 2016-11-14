
package org.coritel.doublylinkedlist;

/**
 * Element not found exception class
 * 
 * @author pedro
 *
 */
@SuppressWarnings("serial")
public class ElementNotFound extends RuntimeException {

    /**
     * Constructor with message
     * @param message
     */
    public ElementNotFound(String message) {
        super(message);
    }

}
