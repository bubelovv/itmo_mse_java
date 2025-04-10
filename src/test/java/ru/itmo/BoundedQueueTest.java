package ru.itmo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class BoundedQueueTest {
    
    private BoundedQueue<Integer> queue;
    
    @BeforeEach
    void setUp() {
        queue = new BoundedQueue<>(3);
    }
    
    @Test
    void testConstructorWithInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedQueue<>(0));
        assertThrows(IllegalArgumentException.class, () -> new BoundedQueue<>(-1));
    }
    
    @Test
    void testInitialState() {
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
        assertNull(queue.poll());
    }
    
    @Test
    void testOfferNull() {
        assertThrows(NullPointerException.class, () -> queue.offer(null));
    }
    
    @Test
    void testOffer() {
        assertTrue(queue.offer(1));
        assertEquals(1, queue.size());
        assertEquals(1, queue.peek());
        
        assertTrue(queue.offer(2));
        assertEquals(2, queue.size());
        assertEquals(1, queue.peek());
        
        assertTrue(queue.offer(3));
        assertEquals(3, queue.size());
        assertEquals(1, queue.peek());
    }
    
    @Test
    void testOfferBeyondCapacity() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        
        assertEquals(3, queue.size());
        assertEquals(2, queue.peek());
        
        queue.offer(5);
        assertEquals(3, queue.size());
        assertEquals(3, queue.peek());
        
        queue.offer(6);
        assertEquals(3, queue.size());
        assertEquals(4, queue.peek());
    }
    
    @Test
    void testPoll() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        assertEquals(1, queue.poll());
        assertEquals(2, queue.size());
        assertEquals(2, queue.peek());
        
        assertEquals(2, queue.poll());
        assertEquals(1, queue.size());
        assertEquals(3, queue.peek());
        
        assertEquals(3, queue.poll());
        assertEquals(0, queue.size());
        assertNull(queue.peek());
        
        assertNull(queue.poll());
    }
    
    @Test
    void testElement() {
        assertThrows(NoSuchElementException.class, () -> queue.element());
        
        queue.offer(1);
        assertEquals(1, queue.element());
    }
    
    @Test
    void testRemove() {
        assertThrows(NoSuchElementException.class, () -> queue.remove());
        
        queue.offer(1);
        assertEquals(1, queue.remove());
        assertTrue(queue.isEmpty());
        
        queue.offer(2);
        queue.offer(3);
        assertEquals(2, queue.remove());
        assertEquals(1, queue.size());
        assertEquals(3, queue.element());
    }
    
    @Test
    void testIterator() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        Iterator<Integer> it = queue.iterator();
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        assertTrue(it.hasNext());
        assertEquals(3, it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
    
    @Test
    void testEmptyIterator() {
        Iterator<Integer> it = queue.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
    
    @Test
    void testConcurrentModificationExceptionWithOffer() {
        queue.offer(1);
        queue.offer(2);
        
        Iterator<Integer> it = queue.iterator();
        assertEquals(1, it.next());
        
        queue.offer(3);
        
        assertThrows(ConcurrentModificationException.class, it::next);
    }
    
    @Test
    void testConcurrentModificationExceptionWithPoll() {
        queue.offer(1);
        queue.offer(2);
        
        Iterator<Integer> it = queue.iterator();
        assertEquals(1, it.next());
        
        queue.poll();
        
        assertThrows(ConcurrentModificationException.class, it::next);
    }
    
    @Test
    void testCircularBehavior() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        assertEquals(1, queue.poll());
        
        queue.offer(4);
        
        assertEquals(2, queue.poll());
        assertEquals(3, queue.poll());
        assertEquals(4, queue.poll());
        
        assertTrue(queue.isEmpty());
        
        queue.offer(5);
        queue.offer(6);
        queue.offer(7);
        
        assertEquals(5, queue.poll());
        assertEquals(6, queue.poll());
        assertEquals(7, queue.poll());
    }
    
    @Test
    void testToString() {
        assertEquals("[]", queue.toString());
        
        queue.offer(1);
        assertEquals("[1]", queue.toString());
        
        queue.offer(2);
        queue.offer(3);
        assertEquals("[1, 2, 3]", queue.toString());
        
        queue.poll();
        assertEquals("[2, 3]", queue.toString());
        
        queue.offer(4);
        assertEquals("[2, 3, 4]", queue.toString());
        
        // Test circular buffer behavior
        queue.poll();
        queue.poll();
        queue.offer(5);
        queue.offer(6);
        assertEquals("[4, 5, 6]", queue.toString());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10})
    void testQueueWithDifferentCapacities(int capacity) {
        BoundedQueue<String> stringQueue = new BoundedQueue<>(capacity);
        
        for (int i = 0; i < capacity; i++) {
            stringQueue.offer("Element-" + i);
            assertEquals(i + 1, stringQueue.size());
        }
        
        stringQueue.offer("Overflow");
        assertEquals(capacity, stringQueue.size());
        
        assertNotEquals("Element-0", stringQueue.peek());
    }
    
    @Test
    void testSequentialOfferAndPoll() {
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
            if (i >= 3) {
                assertEquals(i - 2, queue.peek());
            }
        }
        
        for (int i = 7; i < 10; i++) {
            assertEquals(i, queue.poll());
        }
        
        assertTrue(queue.isEmpty());
    }
    
    @Test
    void testSizeChangesCorrectly() {
        assertEquals(0, queue.size());
        
        queue.offer(1);
        assertEquals(1, queue.size());
        
        queue.offer(2);
        assertEquals(2, queue.size());
        
        queue.offer(3);
        assertEquals(3, queue.size());
        
        queue.offer(4);
        assertEquals(3, queue.size());
        
        queue.poll();
        assertEquals(2, queue.size());
        
        queue.poll();
        assertEquals(1, queue.size());
        
        queue.poll();
        assertEquals(0, queue.size());
    }
    
    @Test
    void testClearByPolling() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        while (!queue.isEmpty()) {
            queue.poll();
        }
        
        assertEquals(0, queue.size());
        assertNull(queue.peek());
        assertNull(queue.poll());
    }
    
    @Test
    void testIteratorRemove() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        Iterator<Integer> it = queue.iterator();
        assertEquals(1, it.next());
        it.remove();
        
        assertEquals(2, queue.size());
        assertEquals(2, queue.peek());
    }
    
    @Test
    void testIteratorRemoveWithoutNext() {
        queue.offer(1);
        queue.offer(2);
        
        Iterator<Integer> it = queue.iterator();
        
        assertThrows(IllegalStateException.class, it::remove);
    }
    
    @Test
    void testIteratorRemoveTwiceAfterOneNext() {
        queue.offer(1);
        queue.offer(2);
        
        Iterator<Integer> it = queue.iterator();
        it.next();
        it.remove();
        
        assertThrows(IllegalStateException.class, it::remove);
    }
    
    @Test
    void testIteratorRemoveMiddleElement() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        Iterator<Integer> it = queue.iterator();
        it.next();
        assertEquals(2, it.next());
        it.remove();
        
        assertEquals(2, queue.size());
        assertEquals(1, queue.peek());
        
        assertEquals(3, it.next());
        assertFalse(it.hasNext());
    }
    
    @Test
    void testIteratorRemoveLastElement() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        Iterator<Integer> it = queue.iterator();
        it.next();
        it.next();
        assertEquals(3, it.next());
        it.remove();
        
        assertEquals(2, queue.size());
        assertEquals(1, queue.peek());
        
        Iterator<Integer> newIt = queue.iterator();
        assertEquals(1, newIt.next());
        assertEquals(2, newIt.next());
        assertFalse(newIt.hasNext());
    }
    
    @Test
    void testIteratorRemoveAllElements() {
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        
        Iterator<Integer> it = queue.iterator();
        
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertNull(queue.peek());
    }
    
    @Test
    void testIteratorRemoveThenAdd() {
        queue.offer(1);
        queue.offer(2);
        
        Iterator<Integer> it = queue.iterator();
        it.next();
        it.remove();
        
        queue.offer(3);
        
        assertEquals(2, queue.size());
        assertEquals(2, queue.peek());
        assertThrows(ConcurrentModificationException.class, it::next);
    }
}