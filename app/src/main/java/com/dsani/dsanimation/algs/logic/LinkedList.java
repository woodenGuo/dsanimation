package com.dsani.dsanimation.algs.logic;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;
    private class Node {
        private Item item;
        private Node next;
    }

    public LinkedList() {
        first = null;
        last = null;
    }
    @NonNull
    @Override
    public Iterator<Item> iterator() {
        return null;
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}
