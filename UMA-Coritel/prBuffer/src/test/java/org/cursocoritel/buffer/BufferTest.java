package org.cursocoritel.buffer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit test class.
 * 
 * @author Pedro Javier Bermudez Araguez
 * @since 2016-11-02
 */
public class BufferTest {
    private Buffer<Integer> bufferTested;
    private static final int BUFFER_SIZE = 5;
    private Logger log;

    /**
     * Initialize Buffer object before each test
     */
    @Before
    public void init() {
        bufferTested = new Buffer<>(BUFFER_SIZE);
        log = Logger.getLogger(BufferTest.class.getName());
    }

    /**
     * Delete Buffer object after each test
     */
    @After
    public void teardown() {
        bufferTested = null;
    }

    /**
     * Constructor should be created
     */
    @Test
    public void testConstructorThenCreated() {
        assertNotNull(bufferTested);
    }

    /**
     * Given a buffer size 0, the constructor must throw an BufferException
     */
    @Test(expected = BufferException.class)
    public void testConstructor0CapacityThenExceptition() {
        int bufferSize = 0;
        Buffer<Integer> bufferQueue = new Buffer(bufferSize);
    }

    /**
     * Given a negative buffer size, the constructor must throw an excepction
     */
    @Test(expected = BufferException.class)
    public void testConstructorNegativeCapacityThenException() {
        Buffer<Integer> bufferQueue = new Buffer(-BUFFER_SIZE);
    }

    /**
     * Testing a given buffer size, buffer size it should be created with the
     * same buffer size
     */
    @Test
    public void testSizeThenEqualToGiven() {
        assertEquals(BUFFER_SIZE, bufferTested.getCapacity());
    }

    /**
     * Testing put method, it should be got the same value given
     */
    @Test
    public void tesMethodPutThenGetGivesEqualValue() {
        Integer number = new Integer(5);
        bufferTested.put(number);
        assertEquals(number, bufferTested.get());
    }

    /**
     * Testing put when size equals buffer size, it should throw an exception
     */
    @Test(expected = BufferException.class)
    public void testCapacityExceededThenExcepcion() {
        Integer number = new Integer(5);
        for (int i = 0; i <= BUFFER_SIZE; i++)
            bufferTested.put(number);
        fail("It Shouldn't be here");
    }

    /**
     * Testing get with empty buffer it should throw an exception
     */
    @Test(expected = BufferException.class)
    public void testGetFromEmptyBufferThenExcepcion() {
        bufferTested.get();
        fail("It shouldn't be here");
    }

    /**
     * Testing getNumberOfOperations() method
     */
    @Test
    public void testGetTotalOperaciones() {
        Integer number = new Integer(5);
        int totalOperationsExpected = 2;
        bufferTested.put(number);
        bufferTested.get();
        assertEquals(totalOperationsExpected,
                        bufferTested.getNumberOfOperations());
    }

    /**
     * Testing getNumberOfElements() method
     */
    @Test
    public void testGetTotalElementos() {
        Integer number = new Integer(5);
        int totalGetElementExpected = 2;
        bufferTested.put(number);
        bufferTested.put(number);
        assertEquals(totalGetElementExpected,
                        bufferTested.getNumberOfElements());
    }

    /**
     * Testing getNumberOfHoles() method
     */
    @Test
    public void testGetTotalHollows() {
        Integer number = new Integer(5);
        int totalHollowExpected = 3;
        bufferTested.put(number);
        bufferTested.put(number);
        assertEquals(totalHollowExpected, bufferTested.getNumberOfHoles());
    }

    /**
     * Testing if get returns a value
     */
    @Test
    public void shouldGetReturnAValueIfTheBufferIsNotEmpty() {
        Integer number = new Integer(5);
        bufferTested.put(number);
        assertNotNull(bufferTested.get());

    }

    /**
     * Testing if it get returns the correct value
     */
    @Test
    public void shouldGetReturnTheRightValueIfTheBufferHasAnElement() {
        int realNumber = 5;
        Integer number = new Integer(realNumber);
        Integer numberExpected = new Integer(realNumber);
        bufferTested.put(number);
        assertEquals(numberExpected, bufferTested.get());
    }
}
