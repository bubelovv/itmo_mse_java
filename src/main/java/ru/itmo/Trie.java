package ru.itmo;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Trie {
    private final Node root = new Node();
    private int size = 0;

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isTerminal;
        int prefixCount = 0;
    }

	/**
	 * Expected complexity: O(|element|)
	 * @return true if this set did not already contain the specified element
	 */
	public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node current = root;
        current.prefixCount++;

        for (char c : element.toCharArray()) {
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
            current.prefixCount++;
        }

        current.isTerminal = true;
        size++;

        return true;
	}

	/**
	 * Expected complexity: O(|element|)
	 */
	public boolean contains(String element) {
        Node current = root;

        for (char c : element.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }

            current = current.children.get(c);
        }

        return current.isTerminal;
	}

	/**
	 * Expected complexity: O(|element|)
	 * @return true if this set contained the specified element
	 */
	public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Node current = root;
        current.prefixCount--;

        for (char c : element.toCharArray()) {
            Node child = current.children.get(c);
            child.prefixCount--;

            if (child.prefixCount == 0) {
                current.children.remove(c);
                break;
            }

            current = child;
        }

        if (current.isTerminal) {
            current.isTerminal = false;
        }

        size--;
        return true;
	}

	/**
	 * Expected complexity: O(1)
	 */
	public int size() {
        return size;
	}

	/**
	 * Expected complexity: O(|prefix|)
	 */
	public int howManyStartsWithPrefix(String prefix) {
        Node current = root;

        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return 0;
            }

            current = current.children.get(c);
        }

        return current.prefixCount;
	}

	/**
	 * Get String in trie, next after [element] in k elements
	 * @return found String or null
	 */
	public String nextString(String element, int k) {
        if (k == 0) {
            return contains(element) ? element : null;
        }

        List<String> strings = getSortedStrings();
        int targetIndex = findTargetIndex(strings, element, k);

        if (targetIndex < strings.size()) {
            return strings.get(targetIndex);
        }

        return null;
	}

    /**
     * Finds the index of [element] in a sorted list, adjusted by [k].
     * @return adjusted index or insertion point
     */
    private int findTargetIndex(List<String> sortedStrings, String element, int k) {
        int position = Collections.binarySearch(sortedStrings, element);
        return position >= 0 ? position + k : -position - 2 + k;
    }

    /**
     * Returns all strings in the trie, sorted lexicographically.
     * @return a sorted list of strings in the trie.
     */
    private List<String> getSortedStrings() {
        List<String> result = new ArrayList<>();
        collectStrings(root, new StringBuilder(), result);
        Collections.sort(result);
        return result;
    }

    /**
     * Recursively collects all strings represented by the Trie starting from the given node.
     * @param node   The current node in the Trie from which to start collecting strings.
     * @param prefix A StringBuilder representing the current prefix being built.
     * @param result A list to store all the collected strings.
     */
    private void collectStrings(Node node, StringBuilder prefix, List<String> result) {
        if (node.isTerminal) {
            result.add(prefix.toString());
        }

        for (char c : node.children.keySet()) {
            prefix.append(c);
            collectStrings(node.children.get(c), prefix, result);
            prefix.setLength(prefix.length() - 1);
        }
    }
}
