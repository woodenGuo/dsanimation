package com.dsani.dsanimation.algs.logic;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private  int n;

    private  class Node{
        Item item;
        private Node next;
    }

    public Queue(){
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty(){
        return  n == 0;
    }

    public int size(){
        return  n;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return first.item;
    }

    public void enqueue(Item item){
        Node oldLast = last;
        last = new Node();
        last.item = item;
        oldLast.next = last;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        n++;
    }

    public Item dequeue(){
        Item item = first.item;
        first = first.next;
        n--;
        // to avoid loitering,
        // if don't do this, next enqueue() will lead to first loiter
        if (isEmpty()) last = null;
        return item;
    }

    @NotNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    @NotNull
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
