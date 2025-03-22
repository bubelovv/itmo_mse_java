package ru.itmo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrieTest {

	@Test
	public void testNextStringExample() {
		Trie trie = new Trie();
		assertNull(trie.nextString("J", 0));
		assertNull(trie.nextString("J", 1));
		trie.add("JUST");
		assertNull(trie.nextString("J", 0));
		assertNull(trie.nextString("J", 1));
		assertNull(trie.nextString("J", 2));
		assertEquals("JUST", trie.nextString("J", 3));
	}
}
