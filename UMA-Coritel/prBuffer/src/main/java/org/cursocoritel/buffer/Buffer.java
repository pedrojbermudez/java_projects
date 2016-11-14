package org.cursocoritel.bufferj;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * 
 * @author Pedro
 *
 * @param <T>
 */
public class Buffer<T> {
    private Queue<T> bufferQueue;
    private int capacity;
    private Logger log;
    private int numberPutOperations = 0;
    private int numberGetOperations = 0;

    /**
     * 
     * @param bufferSize
     */
    public Buffer(int bufferSize) throws BufferException {
        if (bufferSize <= 0)
            throw new BufferException("Buffer size must be 1 or greater");
        log = Logger.getLogger(Buffer.class.getName());
        capacity = bufferSize;
        bufferQueue = new LinkedList<>();
    }

    /**
     * 
     * @param element
     * @throws BufferException
     */
    public void put(T element) throws BufferException {
        if (bufferQueue.size() == capacity)
            throw new BufferException("Capacity limit exceeded");
        log.info("Element inserted");
        bufferQueue.add(element);
        numberPutOperations++;
    }

    /**
     * 
     * @return
     * @throws BufferException
     */
    public T get() throws BufferException {
        if (bufferQueue.isEmpty())
            throw new BufferException("Buffer empty");

        T value = bufferQueue.remove();
        log.info("Element extracted");

        numberGetOperations++;
        return value;
    }

    public int getNumberOfElements() {
        return bufferQueue.size();
    }

    public int getNumberOfHoles() {
        return capacity - bufferQueue.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumberOfOperations() {
        return numberPutOperations + numberGetOperations;
    }
}
