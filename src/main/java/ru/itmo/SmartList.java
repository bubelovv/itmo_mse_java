package ru.itmo;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SmartList<E> extends AbstractList<E> {
    private int size = 0;
    private Object data = null;
    private static final int CAPACITY_ARRAY = 5;

    public SmartList() {
    }

    public SmartList(Collection<E> collection) {
        if (collection != null) {
            addAll(collection);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int idx) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException("Index: " + idx);

        if (size == 1) {
            return (E) data;
        } else if (size <= CAPACITY_ARRAY) {
            return ((E[]) data)[idx];
        } else {
            return ((ArrayList<E>) data).get(idx);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int idx, E el) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException("Index: " + idx);

        E old;

        if (size == 1) {
            old = (E) data;
            data = el;
        } else if (size <= CAPACITY_ARRAY) {
            old = ((E[]) data)[idx];
            ((E[]) data)[idx] = el;
        } else {
            old = ((ArrayList<E>) data).set(idx, el);
        }

        return old;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean add(E el) {
        if (size == 0) {
            data = el;
        } else if (size == 1) {
            Object[] arr = new Object[CAPACITY_ARRAY];
            arr[0] = data;
            arr[1] = el;
            data = arr;
        } else if (size < CAPACITY_ARRAY) {
            E[] arr = (E[]) data;
            arr[size] = el;
        } else if (size == CAPACITY_ARRAY) {
            E[] arr = (E[]) data;
            ArrayList<E> arrayList = new ArrayList<>(size + 1);
            arrayList.addAll(List.of(arr).subList(0, size));
            arrayList.add(el);
            data = arrayList;
        } else {
            ((ArrayList<E>) data).add(size, el);
        }

        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int idx) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException("Index: " + idx);

        E removed;

        if (size == 1) {
            removed = (E) data;
            data = null;
        } else if (size <= CAPACITY_ARRAY) {
            E[] arr = (E[]) data;
            removed = arr[idx];

            if (size == 2) {
                data = idx == 0 ? arr[1] : arr[0];
            } else {
                for (int i = idx; i < size - 1; i++) {
                    arr[i] = arr[i + 1];
                }
                arr[size - 1] = null;
            }
        } else if (size == CAPACITY_ARRAY + 1) {
            ArrayList<E> arrayList = (ArrayList<E>) data;
            Object[] arr = new Object[CAPACITY_ARRAY];
            removed = arrayList.remove(idx);

            for (int i = 0; i < CAPACITY_ARRAY; i++) {
                arr[i] = arrayList.get(i);
            }

            data = arr;
        } else {
            removed = ((ArrayList<E>) data).remove(idx);
        }

        size--;
        return removed;
    }

    @Override
    @SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		if (size == 0) return false;
        if (size == 1) return Objects.equals(data, o);
        if (size < CAPACITY_ARRAY) return ((ArrayList<E>) data).contains(o);

		for (int i = 0; i < size; i++) {
			if (Objects.equals(((E[]) data)[i], o)) return true;
		}

		return false;
	}
}