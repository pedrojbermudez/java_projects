
package org.coritel.doublylinkedlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for DoublyLinkedList class.
 */
public class DoublyLinkedListTest {
    private DoublyLinkedList<Integer> linkedList;
    private Integer number1;
    private Integer number2;
    private Integer number3;

    /**
     * Initializing variables
     */
    @Before
    public void setup() {
        linkedList = new DoublyLinkedList<>();
        number1 = new Integer(48);
        number2 = new Integer(49);
        number3 = new Integer(50);
    }

    /**
     * Destroying objects
     */
    @After
    public void tearDown() {
        linkedList = null;
    }

    /**
     * Testing insertFirstMethod
     */
    @Test
    public void testInsertFirst() {
        Integer number1Expected = new Integer(48);
        Integer number2Expected = new Integer(49);
        Integer number3Expected = new Integer(50);

        linkedList.insertBeginning(number3);
        linkedList.insertBeginning(number2);
        linkedList.insertBeginning(number1);

        assertEquals(number1Expected, linkedList.get(number1Expected));
        assertEquals(number2Expected, linkedList.get(number2Expected));
        assertEquals(number3Expected, linkedList.get(number3Expected));
    }

    /**
     * Testing insertEnd() method
     */
    @Test
    public void testInsertLast() {
        Integer number1Expected = new Integer(48);
        Integer number2Expected = new Integer(49);
        Integer number3Expected = new Integer(50);

        linkedList.insertEnd(number1);
        linkedList.insertEnd(number2);
        linkedList.insertEnd(number3);

        assertEquals(number1Expected, linkedList.get(number1Expected));
        assertEquals(number2Expected, linkedList.get(number2Expected));
        assertEquals(number3Expected, linkedList.get(number3Expected));
    }

    /**
     * Testing remove() method
     */
    @Test
    public void testRemove() {
        Integer number1Expected = new Integer(48);
        Integer number2Expected = new Integer(49);
        Integer number3Expected = new Integer(50);

        linkedList.insertEnd(number1);
        linkedList.insertEnd(number2);
        linkedList.insertEnd(number3);
        linkedList.remove(number3);

        assertEquals(number1Expected, linkedList.get(number1Expected));
        assertEquals(number2Expected, linkedList.get(number2Expected));
        assertNull(linkedList.get(number3Expected));
    }

    /**
     * Testing isEmpty() method
     */
    @Test
    public void testIsEmpty() {
        boolean isEmptyTrue = true;
        assertEquals(isEmptyTrue, linkedList.isEmpty());
    }

    /**
     * Testing isEmpty() method, in this case must return false
     */
    @Test
    public void testIsEmptyFalse() {
        boolean isEmptyFalse = false;
        linkedList.insertBeginning(number1);
        assertEquals(isEmptyFalse, linkedList.isEmpty());
    }

    /**
     * Testing insert after, one good must return true and the other bad must
     * return false
     */
    @Test
    public void testInsertAfter() {
        linkedList.insertBeginning(number1);
        boolean inserted1 = linkedList.insertAfter(number1, number2);
        boolean inserted2 = linkedList.insertAfter(number3, number2);
        boolean inserted1Expected = true;
        boolean inserted2Expected = false;
        assertEquals(inserted1Expected, inserted1);
        assertEquals(inserted2Expected, inserted2);
        assertNull(linkedList.get(number3));
    }

    /**
     * Testing insert before,one good must return true and the other bad must
     * return false
     */
    @Test
    public void testInsertBefore() {
        linkedList.insertEnd(number1);
        linkedList.insertEnd(number2);
        linkedList.insertEnd(number3);
        boolean inserted1 = linkedList.insertBefore(number2, 52);
        boolean inserted2 = linkedList.insertBefore(65, number2);
        boolean inserted1Expected = true;
        boolean inserted2Expected = false;
        assertEquals(inserted1Expected, inserted1);
        assertEquals(inserted2Expected, inserted2);
        assertEquals(new Integer(52), linkedList.get(52));
        assertNull(linkedList.get(65));
    }
}
