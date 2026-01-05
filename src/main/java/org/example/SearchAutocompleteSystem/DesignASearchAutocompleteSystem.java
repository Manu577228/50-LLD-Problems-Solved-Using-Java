package org.example.SearchAutocompleteSystem;

//Design a Search Autocomplete System that suggests the top matching search
//queries as the user types characters.
//Suggestions must be prefix-based, ranked by frequency/popularity, and returned in real time.

import java.util.*;

class Suggestion {
    String word;
    int frequency;

    Suggestion(String w, int f) {
        word = w;
        frequency = f;
    }
}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEnd;
    int frequency;
}

class Trie {
    private final TrieNode root = new TrieNode();

    void insert(String word, int freq) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) node.children[idx] = new TrieNode();
            node = node.children[idx];
        }

        node.isEnd = true;
        node.frequency += freq;
    }

    List<Suggestion> searchPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (node.children[idx] == null) return Collections.emptyList();
            node = node.children[idx];
        }

        List<Suggestion> result = new ArrayList<>();
        dfs(node, new StringBuilder(prefix), result);
        return result;
    }

    private void dfs(TrieNode node, StringBuilder path, List<Suggestion> res) {
        if (node.isEnd) res.add(new Suggestion(path.toString(), node.frequency));

        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                path.append((char) ('a' + i));
                dfs(node.children[i], path, res);
                path.deleteCharAt(path.length() - 1);
            }
        }
    }
}

class SearchService {
    private final Trie trie = new Trie();

    void addSearch(String query, int frequency) {
        trie.insert(query.toLowerCase(), frequency);
    }

    List<String> autocomplete(String prefix, int k) {
        List<Suggestion> list = trie.searchPrefix(prefix.toLowerCase());
        list.sort((a, b) -> b.frequency - a.frequency);

        List<String> res = new ArrayList<>();
        for (int i = 0; i < Math.min(k, list.size()); i++)
            res.add(list.get(i).word);
        return res;
    }
}

class Main {
    public static void main(String[] args) {
        SearchService service = new SearchService();

        service.addSearch("apple", 10);
        service.addSearch("app", 15);
        service.addSearch("application", 5);
        service.addSearch("apex", 7);

        System.out.println(service.autocomplete("ap", 3));
        // Random Input: prefix = "ap"
        // Output: [app, apple, apex]
    }
}
