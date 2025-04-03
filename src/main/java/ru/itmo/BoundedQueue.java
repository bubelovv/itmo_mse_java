package ru.itmo;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Queue;

public class BoundedQueue<T> extends AbstractCollection<T> implements Queue<T> {

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(T t) {
        return false;
    }

    @Override
    public T remove() {
        return null;
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return null;
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}