package com.dsani.dsanimation.algs.logic;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> {

    private Node first;
    private int n;

    private class Node{
        private Item item;
        private Node next;
    }

    public Stack(){
        first = null;
        n = 0;
    }

    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public void push(Item item){
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        n++;
    }

    public Item pop() throws NoSuchFieldException {
        if (isEmpty()) throw  new NoSuchFieldException("Stack underflow");
        Item item = first.item;
        first = first.next;
        n--;
        return item;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return first.item;
    }

    @NotNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    @NonNull
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
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
