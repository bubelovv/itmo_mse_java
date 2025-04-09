package ru.itmo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MultisetTest {

	private static final int RANDOM_SET_SIZE = 100;

	@Test
	public void testSimple() {
		Multiset<Integer> multiset = createMultiset(1, 2, 1);

		assertEquals(3, multiset.size());
		assertEquals(2, multiset.count(1));
		assertEquals(1, multiset.count(2));
		assertEquals(0, multiset.count(3));

		assertTrue(multiset.contains(1));
		assertTrue(multiset.contains(2));
		assertFalse(multiset.contains(3));
	}

	@Test
	public void testRemove() {
		Multiset<Integer> multiset = createMultiset(1, 2, 1);
		assertTrue(multiset.remove(1));

		assertEquals(2, multiset.size());
		assertEquals(1, multiset.count(1));
		assertEquals(1, multiset.count(2));

		assertTrue(multiset.contains(1));
		assertTrue(multiset.contains(2));

		assertFalse(multiset.contains(3));
	}

	@Test
	public void testSimpleIterator() {
		Multiset<Integer> multiset = createMultiset(1, 2, 1);
		assertMultisetState(multiset, 1, 1, 2);
	}

	@Test
	public void testSimpleEntrySet() {
		Multiset<Integer> multiset = createMultiset(1, 2, 1);

		Set<Multiset.Entry<Integer>> entries = multiset.entrySet();

		assertEquals(2, entries.size());
	}

	@Test
	public void testSimpleEntryIterator() {
		Multiset<Integer> multiset = createMultiset(3, 1, 2, 1, 2, 1);

		@SuppressWarnings("unchecked")
		Multiset.Entry<Integer>[] entries = new ArrayList<>(multiset.entrySet()).toArray(new Multiset.Entry[2]);

		assertEquals(3, entries.length);

		assertEquals(3, entries[0].getElement());
		assertEquals(1, entries[0].getCount());

		assertEquals(1, entries[1].getElement());
		assertEquals(3, entries[1].getCount());

		assertEquals(2, entries[2].getElement());
		assertEquals(2, entries[2].getCount());
	}

	@Test
	public void testEntryIteratorRemove() {
		Multiset<Integer> multiset = createMultiset(3, 1, 2, 1, 2, 1);

		Iterator<Multiset.Entry<Integer>> iterator = multiset.entrySet().iterator();

		assertTrue(iterator.hasNext());
		Multiset.Entry<Integer> next = iterator.next();
		assertEquals(3, next.getElement());
		assertEquals(1, next.getCount());
		iterator.remove();
		assertMultisetState(multiset, 1, 1, 1, 2, 2);

		next = iterator.next();
		assertEquals(1, next.getElement());
		assertEquals(3, next.getCount());
		iterator.remove();
		assertMultisetState(multiset, 2, 2);

		next = iterator.next();
		assertEquals(2, next.getElement());
		assertEquals(2, next.getCount());
		iterator.remove();
		assertMultisetState(multiset);
	}

	@Test
	public void testIteratorRemove() {
		Multiset<Integer> multiset = createMultiset();
		multiset.add(1);
		multiset.add(2);
		multiset.add(1);

		Iterator<Integer> iterator = multiset.iterator();

		assertMultisetState(multiset, 1, 1, 2);
		removeAndAssertState(iterator, 1, multiset, 1, 2);
		removeAndAssertState(iterator, 1, multiset, 2);
		removeAndAssertState(iterator, 2, multiset);
	}

	private static void removeAndAssertState(Iterator<Integer> iterator, Integer elementToBeRemoved, Multiset<Integer> multiset, Integer... state) {
		assertTrue(iterator.hasNext());
		assertEquals(elementToBeRemoved, iterator.next());
		iterator.remove();

		assertMultisetState(multiset, state);
	}

	@Test
	public void testIteratorRemoveComplex() {
		Multiset<Integer> multiset = createMultiset(1, 2, 3, 2, 1, 1);

		Iterator<Integer> iterator = multiset.iterator();

		assertMultisetState(multiset, 1, 1, 1, 2, 2, 3);
		removeAndAssertState(iterator, 1, multiset, 1, 1, 2, 2, 3);
		removeAndAssertState(iterator, 1, multiset, 1, 2, 2, 3);
		removeAndAssertState(iterator, 1, multiset, 2, 2, 3);
		removeAndAssertState(iterator, 2, multiset, 2, 3);
		removeAndAssertState(iterator, 2, multiset, 3);
		removeAndAssertState(iterator, 3, multiset);
	}

	@Test
	public void testRandomSet() {
		Multiset<Integer> multiset = createMultiset();
		ArrayList<Integer> list = new ArrayList<>();
		Random random = new Random(123);

		for (int i = 0; i < RANDOM_SET_SIZE; i++) {
			int anInt = random.nextInt();
			assertTrue(multiset.add(anInt));
			list.add(anInt);
		}

		assertEquals(RANDOM_SET_SIZE, list.size());
		for (Integer integer : list) {
			assertTrue(multiset.contains(integer));
			assertTrue(multiset.count(integer) >= 1);

			assertTrue(multiset.remove(integer));
		}

		assertTrue(multiset.isEmpty());
		assertMultisetState(multiset);
	}

	@Test
	public void testRandomEntrySet() {
		Multiset<Integer> multiset = createMultiset();
		ArrayList<Integer> list = new ArrayList<>();
		Random random = new Random(123);

		for (int i = 0; i < RANDOM_SET_SIZE; i++) {
			int anInt = random.nextInt();
			assertTrue(multiset.add(anInt));
			list.add(anInt);
		}

		assertEquals(RANDOM_SET_SIZE, list.size());
		Set<Multiset.Entry<Integer>> entries = multiset.entrySet();
		assertThrows(UnsupportedOperationException.class, () -> entries.add(entries.iterator().next()));

		for (Integer integer : list) {
			Multiset.Entry<Integer> integerEntry = findEntry(entries, integer);
			assertEquals(integerEntry.getCount(), countEqualsTo(list, integer));
		}

		for (Multiset.Entry<Integer> entry : entries) {
			assertEquals(entry.getCount(), countEqualsTo(list, entry.getElement()));
		}
	}

	@Test
	public void testIteratorRemoveWithoutNext() {
		final Multiset<Integer> multiset = createMultiset(1, 2);

		final Iterator<Integer> iterator = multiset.iterator();

		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class, iterator::remove);
	}

	@Test
	public void testIteratorSameRemoveWithoutNext() {
		final Multiset<Integer> multiset = createMultiset(1, 1);

		final Iterator<Integer> iterator = multiset.iterator();

		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class, iterator::remove);
	}

	@Test
	public void testIteratorNextIfHasNotNext() {
		final Multiset<Integer> multiset = createMultiset(1);

		final Iterator<Integer> iterator = multiset.iterator();

		iterator.next();
		assertThrows(NoSuchElementException.class, iterator::next);
	}

	@Test
	public void testEntrySetIteratorRemoveWithoutNext() {
		final Set<Multiset.Entry<Integer>> entries = createMultiset(1, 1).entrySet();

		final Iterator<Multiset.Entry<Integer>> iterator = entries.iterator();

		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class, iterator::remove);
	}

	@Test
	public void testEntrySetIteratorSameRemoveWithoutNext() {
		final Set<Multiset.Entry<Integer>> entries = createMultiset(1, 2).entrySet();

		final Iterator<Multiset.Entry<Integer>> iterator = entries.iterator();

		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class, iterator::remove);
	}

	@Test
	public void testEntrySetIteratorNextIfHasNotNext() {
		final Set<Multiset.Entry<Integer>> entries = createMultiset(1).entrySet();

		final Iterator<Multiset.Entry<Integer>> iterator = entries.iterator();

		iterator.next();
		assertThrows(NoSuchElementException.class, iterator::next);
	}

	@Test
	public void testIteratorNextNextRemoveNext() {
		final Multiset<Integer> ms = createMultiset(1, 1, 1);
		final Iterator<Integer> iterator = ms.iterator();
		final ArrayList<Integer> result = new ArrayList<>();
		result.add(iterator.next());
		result.add(iterator.next());
		iterator.remove();
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}

		assertEquals(Arrays.asList(1, 1, 1), result);
	}

	@Test
	public void testNull() {
		Multiset<Integer> multiset = createMultiset(null, null, null);
		assertEquals(3, multiset.count(null));

		Iterator<Integer> iterator = multiset.iterator();

		assertMultisetState(multiset, null, null, null);
		removeAndAssertState(iterator, null, multiset, null, null);
		removeAndAssertState(iterator, null, multiset, new Integer[]{null});
		removeAndAssertState(iterator, null, multiset);
	}

	@Test
	public void testIteratorNextRemoveSameElementBalance() {
		final Iterator<Integer> iterator = createMultiset(1, 1, 1, 1, 1, 1).iterator();

		iterator.next();
		iterator.next();
		iterator.next();
		iterator.next();

		iterator.remove();
		assertThrows(IllegalStateException.class, iterator::remove);
	}

	@Test
	public void testComplexElementSet() {
		Multiset<Integer> multiset = createMultiset(1, 2, 3, 1, 3, 1);
		Set<Integer> elementSet = multiset.elementSet();
		assertEquals(3, elementSet.size());
		assertEquals(Arrays.asList(1, 2, 3), new ArrayList<>(elementSet));

		Iterator<Integer> iterator = elementSet.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next());
		iterator.remove();
		assertMultisetState(multiset, 2, 3, 3);
		assertEquals(2, elementSet.size());

		assertTrue(iterator.hasNext());
		assertEquals(2, iterator.next());
		iterator.remove();
		assertMultisetState(multiset, 3, 3);
		assertEquals(1, elementSet.size());

		assertTrue(iterator.hasNext());
		assertEquals(3, iterator.next());
		iterator.remove();
		assertMultisetState(multiset);
		assertEquals(0, elementSet.size());

		assertFalse(iterator.hasNext());
	}

	private Multiset.Entry<Integer> findEntry(Set<Multiset.Entry<Integer>> entries, Integer integer) {
		for (Multiset.Entry<Integer> entry : entries) {
			if (entry.getElement().equals(integer)) return entry;
		}

		fail("Can't find entry for " + integer);
		throw new IllegalStateException();
	}

	private static void assertMultisetState(Multiset<Integer> multiset, Integer... integers) {
		assertEquals(integers.length, multiset.size());
		assertEquals(new ArrayList<>(multiset), Arrays.asList(integers));
	}

	private static int countEqualsTo(Collection<Integer> collection, int element) {
		int result = 0;
		for (Integer integer : collection) {
			if (integer.equals(element)) {
				result++;
			}
		}

		return result;
	}

	@SafeVarargs
	private static <E> Multiset<E> createMultiset(E... data) {
		Multiset<E> multiset = new Multiset<>();

		for (E e : data) {
			assertTrue(multiset.add(e));
		}

		return multiset;
	}
}
