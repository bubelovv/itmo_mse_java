package ru.itmo;

import java.util.AbstractCollection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.AbstractSet;
import java.util.NoSuchElementException;

public class Multiset<E> extends AbstractCollection<E> {
    private final Map<E, Integer> elementToCount = new LinkedHashMap<>();
    private int size = 0;

    /**
     * Returns the number of occurrences of an element in this multiset.
     * <p>
     * Expected complexity: Same as `contains`
     */
    public int count(Object element) {
        @SuppressWarnings("unchecked")
        E item = (E) element;
        Integer count = elementToCount.get(item);
        return count == null ? 0 : count;
    }

    /**
     * Returns the set of distinct elements contained in this multiset.
     * <p>
     * Expected complexity: O(1)
     */
    public Set<E> elementSet() {
        return new ElementSetView();
    }

    /**
     * Returns the set of entries representing the data of this multiset.
     * <p>
     * Expected complexity: O(1)
     */
    public Set<Entry<E>> entrySet() {
        return new EntrySetView();
    }

    /**
     * Elements that occur multiple times in the multiset will appear multiple times in this iterator.
     * <p>
     * Expected complexity: O(1)
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private final Iterator<Map.Entry<E, Integer>> entries = elementToCount.entrySet().iterator();
            private Map.Entry<E, Integer> currentEntry;
            private int remaining = 0;
            private boolean canRemove;

            @Override
            public boolean hasNext() {
                return remaining > 0 || entries.hasNext();
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();

                if (remaining == 0) {
                    currentEntry = entries.next();
                    remaining = currentEntry.getValue();
                }

                remaining--;
                canRemove = true;
                return currentEntry.getKey();
            }

            @Override
            public void remove() {
                if (!canRemove) throw new IllegalStateException();

                canRemove = false;
                int count = currentEntry.getValue();

                if (count > 1) {
                    currentEntry.setValue(count - 1);
                } else {
                    entries.remove();
                }

                size--;
            }
        };
    }

    private class ElementSetView extends AbstractSet<E> {
        @Override
        public int size() {
            return elementToCount.size();
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<>() {
                private final Iterator<E> iter = elementToCount.keySet().iterator();
                private E current;

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public E next() {
                    return current = iter.next();
                }

                @Override
                public void remove() {
                    size -= count(current);
                    iter.remove();
                }
            };
        }
    }

    private class EntrySetView extends AbstractSet<Entry<E>> {
        @Override
        public int size() {
            return elementToCount.size();
        }

        @Override
        public Iterator<Entry<E>> iterator() {
            return new Iterator<>() {
                private final Iterator<Map.Entry<E, Integer>> iter = elementToCount.entrySet().iterator();
                private Map.Entry<E, Integer> current;

                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Entry<E> next() {
                    current = iter.next();
                    return new Entry<>(current.getKey(), current.getValue());
                }

                @Override
                public void remove() {
                    size -= current.getValue();
                    iter.remove();
                }
            };
        }
    }

    public static class Entry<E> {
        private final E element;
        private final int count;

        Entry(E element, int count) {
            this.element = element;
            this.count = count;
        }

        public E getElement() {
            return element;
        }

        public int getCount() {
            return count;
        }
    }

    @Override
    public boolean add(E element) {
        elementToCount.put(element, count(element) + 1);
        size++;
        return true;
    }

    @Override
    public boolean contains(Object element) {
        @SuppressWarnings("unchecked")
        E item = (E) element;
        return elementToCount.containsKey(item);
    }

    @Override
    public boolean remove(Object element) {
        int elemCount = count(element);
        if (elemCount == 0) return false;

        if (elemCount == 1) {
            elementToCount.remove(element);
        } else {
            @SuppressWarnings("unchecked")
            E item = (E) element;
            elementToCount.put(item, elemCount - 1);
        }
        size--;

        return true;
    }

    @Override
    public int size() {
        return size;
    }
}
