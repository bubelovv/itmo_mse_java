package ru.itmo;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class Multiset<E> extends AbstractCollection<E> {
	/**
	 * Returns the number of occurrences of an element in this multiset.
	 * <p>
	 * Expected complexity: Same as `contains`
	 */
	public int count(Object element) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Returns the set of distinct elements contained in this multiset.
	 * <p>
	 * Expected complexity: O(1)
	 */
	public Set<E> elementSet() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Returns the set of entries representing the data of this multiset.
	 * <p>
	 * Expected complexity: O(1)
	 */
	public Set<Entry<E>> entrySet() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Elements that occur multiple times in the multiset will appear multiple times in this iterator.
	 * <p>
	 * Expected complexity: O(1)
	 */
	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public static class Entry<E> {

		public E getElement() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public int getCount() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
}
