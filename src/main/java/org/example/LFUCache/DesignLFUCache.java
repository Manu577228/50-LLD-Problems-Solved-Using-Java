package org.example.LFUCache;//Design an LFU (Least Frequently Used) Cache that supports get(key) and put(key, value) in O(1) time.
//When the cache is full, it should evict the least
//frequently used key; if there’s a tie, evict the least recently used among them.

import java.util.*;

class LFUCache {
    private class Node {
        int k, v, f;
        Node p, n;

        Node(int k, int v) {
            this.k = k;
            this.v = v;
            this.f = 1;
        }
    }

    private class DLL {
        Node h, t;
        int sz;

        DLL() {
            h = new Node(0, 0);
            t = new Node(0, 0);
            h.n = t;
            t.p = h;
        }

        void add(Node x) {
            x.n = h.n;
            x.p = h;
            h.n.p = x;
            h.n = x;
            sz++;
        }

        void remove(Node x) {
            x.p.n = x.n;
            x.n.p = x.p;
            sz--;
        }

        Node removeLast() {
            if (sz == 0) return null;
            Node x = t.p;
            remove(x);
            return x;
        }
    }

    private int cap, minF;
    private Map<Integer, Node> mp;
    private Map<Integer, DLL> fm;

    public LFUCache(int capacity) {
        cap = capacity;
        minF = 0;
        mp = new HashMap<>();
        fm = new HashMap<>();
    }

    public int get(int key) {
        if (!mp.containsKey(key)) return -1;
        Node x = mp.get(key);
        update(x);
        return x.v;
    }

    public void put(int key, int value) {
        if (cap == 0) return;

        if (mp.containsKey(key)) {
            Node x = mp.get(key);
            x.v = value;
            update(x);
        } else {
            if (mp.size() == cap) {
                DLL dl = fm.get(minF);
                Node evict = dl.removeLast();
                mp.remove(evict.k);
            }

            Node x = new Node(key, value);
            mp.put(key, x);
            fm.computeIfAbsent(1, z -> new DLL()).add(x);
            minF = 1;
        }
    }

    private void update(Node x) {
        int f = x.f;
        DLL dl = fm.get(f);
        dl.remove(x);

        if (f == minF && dl.sz == 0) minF++;

        x.f++;
        fm.computeIfAbsent(x.f, z -> new DLL()).add(x);
    }

    public static void main(String[] args) {
        LFUCache c = new LFUCache(2);
        c.put(1, 10);
        c.put(2, 20);
        System.out.println(c.get(1)); // 10
        c.put(3, 30);                 // evicts key 2
        System.out.println(c.get(2)); // -1
        System.out.println(c.get(3)); // 30
    }
}