package ru.itmo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SmartListTest {

	@Test
	public void testSimple() {
		List<Integer> list = new SmartList<>();

		assertEquals(Collections.<Integer>emptyList(), list);

		list.add(1);
		assertEquals(Collections.singletonList(1), list);

		list.add(2);
		assertEquals(Arrays.asList(1, 2), list);
	}

	@Test
	public void testGetSet() {
		List<Object> list = new SmartList<>();

		list.add(1);

		assertEquals(1, list.get(0));
		assertEquals(1, list.set(0, 2));
		assertEquals(2, list.get(0));
		assertEquals(2, list.set(0, 1));

		list.add(2);

		assertEquals(1, list.get(0));
		assertEquals(2, list.get(1));

		assertEquals(1, list.set(0, 2));

		assertEquals(Arrays.asList(2, 2), list);
	}

	@Test
	public void testRemove() {
		List<Object> list = new SmartList<>();

		list.add(1);
		list.remove(0);
		assertEquals(Collections.emptyList(), list);

		list.add(2);
		list.remove((Object) 2);
		assertEquals(Collections.emptyList(), list);

		list.add(1);
		list.add(2);
		assertEquals(Arrays.asList(1, 2), list);

		list.remove(0);
		assertEquals(Collections.singletonList(2), list);

		list.remove(0);
		assertEquals(Collections.emptyList(), list);
	}

	@Test
	public void testIteratorRemove() {
		List<Object> list = new SmartList<>();
		assertFalse(list.iterator().hasNext());

		list.add(1);

		Iterator<Object> iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next());

		iterator.remove();
		assertFalse(iterator.hasNext());
		assertEquals(Collections.emptyList(), list);

		list.addAll(Arrays.asList(1, 2));

		iterator = list.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(1, iterator.next());

		iterator.remove();
		assertTrue(iterator.hasNext());
		assertEquals(Collections.singletonList(2), list);
		assertEquals(2, iterator.next());

		iterator.remove();
		assertFalse(iterator.hasNext());
		assertEquals(Collections.emptyList(), list);
	}


	@Test
	public void testCollectionConstructor() {
		assertEquals(Collections.emptyList(), new SmartList<>(Collections.emptyList()));
		assertEquals(
				Collections.singletonList(1),
				new SmartList<>(Collections.singletonList(1)));

		assertEquals(
				Arrays.asList(1, 2),
				new SmartList<>(Arrays.asList(1, 2)));
	}

	@Test
	public void testAddManyElementsThenRemove() {
		List<Object> list = new SmartList<>();
		for (int i = 0; i < 7; i++) {
			list.add(i + 1);
		}

		assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), list);

		for (int i = 0; i < 7; i++) {
			list.remove(list.size() - 1);
			assertEquals(6 - i, list.size());
		}

		assertEquals(Collections.emptyList(), list);
	}
}