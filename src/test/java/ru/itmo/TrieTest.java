package ru.itmo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrieTest {

	@Test
    public void testAddContainsRemove() {
        Trie trie = new Trie();
        assertTrue(trie.add("abc"));
        assertTrue(trie.add("abcd"));
        assertTrue(trie.add("zzz"));
        assertFalse(trie.add("abc"));  // already present

        assertTrue(trie.contains("abc"));
        assertFalse(trie.contains("ab"));
        assertFalse(trie.contains("aaa"));

        assertTrue(trie.remove("abc"));
        assertFalse(trie.remove("abc")); // already removed
        assertFalse(trie.contains("abc"));
    }

    @Test
    public void testSize() {
        Trie trie = new Trie();
        trie.add("abc");
        trie.add("ab");
        trie.add("zzz");
        assertEquals(3, trie.size());
        trie.remove("ab");
        assertEquals(2, trie.size());
        trie.remove("ab");
        assertEquals(2, trie.size());
        trie.remove("a");
        assertEquals(2, trie.size());
    }

	@Test
    public void testPrefix() {
        Trie trie = new Trie();
        trie.add("ab");
        trie.add("abc");
        trie.add("abcd");
        trie.add("abx");
        trie.add("abxy");
        trie.add("zzz");
        assertEquals(5, trie.howManyStartsWithPrefix("ab"));
        assertEquals(2, trie.howManyStartsWithPrefix("abc"));
        assertEquals(0, trie.howManyStartsWithPrefix("aza"));
    }

    @Test
    public void testEmptyTrieOperations() {
        Trie trie = new Trie();

        assertEquals(0, trie.size());
        assertFalse(trie.contains(""));
        assertFalse(trie.contains("abc"));

        assertNull(trie.nextString("", 0));
        assertNull(trie.nextString("asd", 0));
        assertNull(trie.nextString("abc", 1));
    }

    @Test
    public void testAddEmptyString() {
        Trie trie = new Trie();

        assertTrue(trie.add(""));
        assertTrue(trie.contains(""));
        assertEquals(1, trie.size());

        assertEquals("", trie.nextString("", 0));
    }

    @Test
    public void testRemoveNonExisting() {
        Trie trie = new Trie();

        trie.add("abc");
        assertEquals(1, trie.size());
        assertFalse(trie.remove("abcd"));
        assertEquals(1, trie.size());
        assertTrue(trie.remove("abc"));
        assertEquals(0, trie.size());
    }

    @Test
    public void testHowManyStartsWithPrefixEdges() {
        Trie trie = new Trie();

        trie.add("a");
        trie.add("ab");
        trie.add("abc");
        trie.add("abcd");

        assertEquals(4, trie.howManyStartsWithPrefix(""));
        assertEquals(0, trie.howManyStartsWithPrefix("zzz"));
        assertEquals(0, trie.howManyStartsWithPrefix("a "));
    }

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

    @Test
    public void testNextString() {
        Trie trie = new Trie();
        trie.add("abc");
        trie.add("bcd");
        trie.add("zzz");

        assertEquals("abc", trie.nextString("abc", 0));
        assertNull(trie.nextString("xxx", 0));

        assertEquals("zzz", trie.nextString("bcd", 3));
        assertNull(trie.nextString("zzz", 1));

        assertNull(trie.nextString("bce", 1));
        assertNull(trie.nextString("zzzz", 1));
    }

    @Test
    public void testNextStringEdgeCases() {
        Trie trie = new Trie();

        trie.add("a");
        trie.add("ab");
        trie.add("abc");

        // k > 0, элемент есть
        // "a" -> ["a", "ab", "abc"] -> "abc" через 2 позиции
        assertEquals("abc", trie.nextString("a", 2));

        // k > 0, элемент нет
        // Пусть добавлена "", "a", "ab", "abc" => "aa" не существует.
        // Если бы "aa" была вставлена, она бы шла после "a", но перед "ab".
        // Через 1 позицию стояла бы "ab".
        assertEquals("ab", trie.nextString("aa", 1));

        // Выход за границы (после "abc" ничего нет)
        assertNull(trie.nextString("abc", 1));
    }
}
