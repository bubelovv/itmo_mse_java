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
        assertEquals("JUST", trie.nextString("J", 1));
        assertNull(trie.nextString("J", 2));
        assertNull(trie.nextString("J", 3));
    }

    @Test
    public void testNextString() {
        Trie trie = new Trie();

        trie.add("abc");
        trie.add("bcd");
        trie.add("zzz");

        assertEquals("abc", trie.nextString("abc", 0));
        assertNull(trie.nextString("xxx", 0));
        assertEquals("zzz", trie.nextString("bcd", 1));
        assertEquals("zzz", trie.nextString("zzz", 0));
        assertNull(trie.nextString("zzz", 1));
        assertEquals("zzz", trie.nextString("bce", 1));
        assertNull(trie.nextString("zzzz", 1));
    }

    @Test
    public void testNextStringEdgeCases() {
        Trie trie = new Trie();

        trie.add("a");
        trie.add("ab");
        trie.add("abc");

        assertEquals("abc", trie.nextString("a", 2));
        assertEquals("ab", trie.nextString("aa", 1));
        assertNull(trie.nextString("abc", 1));
    }

    @Test
    public void test6() {
        Trie trie = new Trie();
        trie.add("AA");
        trie.add("AB");
        assertEquals("AA", trie.nextString("A", 1));
        assertEquals("AB", trie.nextString("AB", 0));
    }

    @Test
    public void testNumbers() {
        Trie trie = new Trie();
        
        assertTrue(trie.add("001"));
        assertTrue(trie.add("010"));
        assertTrue(trie.add("101"));
        assertFalse(trie.add("101"));
        
        assertEquals(3, trie.size());
        assertEquals(2, trie.howManyStartsWithPrefix("0"));
        assertEquals("010", trie.nextString("01", 1));
        assertEquals("101", trie.nextString("011", 1));
    }

    @Test
    public void testRemoveAndReinsert() {
        Trie trie = new Trie();
        
        assertTrue(trie.add("test1"));
        assertTrue(trie.add("test2"));
        assertTrue(trie.add("test3"));
        
        assertTrue(trie.remove("test2"));
        assertEquals("test3", trie.nextString("test1", 1));
        
        assertTrue(trie.add("test2"));
        assertEquals("test2", trie.nextString("test1", 1));
        assertEquals("test3", trie.nextString("test1", 2));
    }

    @Test
    public void testComplexPrefixScenario() {
        Trie trie = new Trie();
        
        trie.add("a");
        trie.add("aa");
        trie.add("aaa");
        trie.add("aaaa");
        
        assertEquals(4, trie.howManyStartsWithPrefix("a"));
        assertTrue(trie.remove("aa"));
        assertEquals(3, trie.howManyStartsWithPrefix("a"));
        
        trie.add("ab");
        assertEquals(2, trie.howManyStartsWithPrefix("aa"));
        assertEquals("aaa", trie.nextString("a", 1));
    }

    @Test
    public void testNextStringWithGaps() {
        Trie trie = new Trie();
        
        trie.add("a");
        trie.add("c");
        trie.add("e");
        trie.add("g");
        
        assertEquals("c", trie.nextString("b", 1));
        assertEquals("e", trie.nextString("b", 2));
        assertEquals("g", trie.nextString("f", 1));
        assertNull(trie.nextString("g", 1));
        assertEquals("c", trie.nextString("a", 1));
    }

    @Test
    public void testMixedCaseStrings() {
        Trie trie = new Trie();
        
        trie.add("test");
        trie.add("Test");
        trie.add("TEST");
        
        assertEquals(3, trie.size());
        assertTrue(trie.contains("test"));
        assertTrue(trie.contains("Test"));
        assertTrue(trie.contains("TEST"));
        
        assertEquals("Test", trie.nextString("TEST", 1));
    }

    @Test
    public void testPrefixOverlap() {
        Trie trie = new Trie();
        
        trie.add("prefix");
        trie.add("pre");
        trie.add("prefix1");
        trie.add("prefix2");
        
        assertEquals(4, trie.howManyStartsWithPrefix("pre"));
        assertEquals(3, trie.howManyStartsWithPrefix("prefix"));
        
        assertTrue(trie.remove("prefix"));
        assertEquals(3, trie.howManyStartsWithPrefix("pre"));
        assertEquals(2, trie.howManyStartsWithPrefix("prefix"));
        
        assertEquals("prefix2", trie.nextString("prefix1", 1));
    }
}
