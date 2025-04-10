package ru.itmo;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class BoundedQueue<T> extends AbstractCollection<T> implements Queue<T> {
    private final T[] elements;
    private int size;
    private int head;
    private int tail;
    private int modCount;

    @SuppressWarnings("unchecked")
    public BoundedQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");

        this.elements = (T[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.modCount = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;
            private int lastReturnedIndex = -1;
            private int expectedModCount = modCount;
            private boolean canRemove = false;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (modCount != expectedModCount) throw new ConcurrentModificationException();
                if (!hasNext()) throw new NoSuchElementException();

                int index = (head + currentIndex) % elements.length;
                lastReturnedIndex = currentIndex;
                currentIndex++;
                canRemove = true;
                return elements[index];
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException("remove() can only be called once after next()");
                if (modCount != expectedModCount) throw new ConcurrentModificationException();

                for (int i = lastReturnedIndex; i < size - 1; i++) {
                    int currPos = nextIndex(head, i);
                    int nextPos = nextIndex(head, i + 1);
                    elements[currPos] = elements[nextPos];
                }

                tail = prevIndex(tail, 1);
                elements[tail] = null;
                modCount++;
                size--;

                currentIndex--;
                expectedModCount = modCount;
                canRemove = false;
                lastReturnedIndex = -1;
            }
        };
    }

    private int nextIndex(int index, int step) {
        return (index + step) % elements.length;
    }

    private int prevIndex(int index, int step) {
        return (index - step + elements.length) % elements.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean offer(T t) {
        if (t == null) throw new NullPointerException("Cannot add null element to queue");

        modCount++;

        if (size < elements.length) {
            elements[tail] = t;
            tail = nextIndex(tail, 1);
            size++;
        } else {
            elements[head] = t;
            head = nextIndex(head, 1);
            tail = nextIndex(tail, 1);
        }

        return true;
    }

    @Override
    public T remove() {
        if (isEmpty()) throw new NoSuchElementException();
        return poll();
    }

    @Override
    public T poll() {
        if (isEmpty()) return null;

        modCount++;
        T element = elements[head];
        elements[head] = null;
        head = nextIndex(head, 1);
        size--;

        return element;
    }

    @Override
    public T element() {
        if (isEmpty()) throw new NoSuchElementException();
        return peek();
    }

    @Override
    public T peek() {
        return isEmpty() ? null : elements[head];
    }

    @Override
    public String toString() {
        return super.toString();
    }
}