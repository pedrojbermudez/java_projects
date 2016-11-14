
package org.coritel.doublylinkedlist;

import java.util.logging.Logger;

/**
 * Doubly linked list main class
 * 
 * @author Pedro Javier Bermudez Araguez
 * @version 1.0
 * Current date: 10/11/2016
 *
 * @param <T>
 */
public class DoublyLinkedList<T> {
    private Link first;
    private Link last;

    /**
     * Class to keep tracking of each element
     * 
     * @author Pedro Javier Bermudez Araguez
     * @version 1.0
     */
    class Link {
        private T data;
        private Link prev;
        private Link next;

        /**
         * Constructor
         * 
         * @param data
         */
        public Link(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    /**
     * Insert an element at the beginning of the list
     * 
     * @param element
     */
    public void insertBeginning(T element) {
        Link newLink = new Link(element);
        if (isEmpty())
            last = newLink;
        else first.prev = newLink;
        newLink.next = first;
        first = newLink;
    }

    /**
     * Insert at the end of the linked list
     * 
     * @param element
     */
    public void insertEnd(T element) {
        Link newLink = new Link(element);
        if (isEmpty())
            first = newLink;
        else {
            last.next = newLink;
            newLink.prev = last;
        }
        last = newLink;
    }

    /**
     * Insert before the specified item
     * 
     * @param elementRef
     * @param element
     * @return
     */
    public boolean insertBefore(T elementRef, T element) {
        Link current = first;
        boolean found = true;
        while (found && !current.data.equals(elementRef)) {
            current = current.next;
            if (current == null) 
                found = false;
        }
        if (found) {
            Link newLink = new Link(element);
            if (current == first) {
                newLink.prev = null;
                first = newLink;
                newLink.next = current;
            } else {
                newLink.prev = current.prev;
                newLink.next = current;
                current.prev.next = newLink;
                current.prev = newLink;
            }
        }
        return found;
    }

    /**
     * Insert after the specified item
     * 
     * @param elementRef
     * @param element
     * @return
     */
    public boolean insertAfter(T elementRef, T element) {
        Link current = first;
        boolean found = true;
        while (found && !current.data.equals(elementRef)) {
            current = current.next;
            if(current == null)
                found = false;
        }
        if (found) {
            Link newLink = new Link(element);
            if (current == last) {
                newLink.next = null;
                last = newLink;
            } else {
                newLink.next = current.next;
                current.next.prev = newLink;
            }
            newLink.prev = current;
            current.next = newLink;
        }
        return found;
    }

    /**
     * Get a specified element
     * @param element
     * @return
     */
    public T get(T element) {
        Link current = first;
        while (current != null && !current.data.equals(element))
            current = current.next;
        return current == null ? null : current.data;
    }

    /**
     * Cheking if the linked list is empty
     * 
     * @return
     */
    public boolean isEmpty() {
        return this.first == null;
    }

    /**
     * Remove a specified element
     * 
     * @param element
     * @return
     */
    public Link remove(T element) {
        Link current = first;
        while (!current.data.equals(element)){
            current = current.next;
            if(current == null){
                throw new ElementNotFound("The element was not found")  ;
            }
        }
        if (current == first)
            first = current.next;
        else current.prev.next = current.next;
        if (current == last)
            last = current.prev;
        else current.next.prev = current.prev;
        return current;
    }

    /**
     * Print the linked list to the console. The format is [element1, element2,
     * ..., elementN]
     */
    public void print() {
        Logger log = Logger.getLogger(DoublyLinkedList.class.getName());
        StringBuilder sbPrint = new StringBuilder("[");
        boolean firstTime = true;
        Link current = first;
        while (current != null) {
            if (firstTime) {
                firstTime = false;
                sbPrint.append(current);
            } else
                sbPrint.append(", " + current);
            current = current.next;
        }
        sbPrint.append("]");
        log.info(sbPrint.toString());
    }
}
