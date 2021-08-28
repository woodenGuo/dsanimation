package com.dsani.dsanimation.algs.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class SeqList<Item> implements Iterable<Item> {
    private ArrayList<Item> seq;
    private int size;
    public SeqList(){
        seq = new ArrayList<Item>();
        size = 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void insert(Item item, int index){
        seq.add(index, item);
        size++;
    }

    public Item get(int index){
        return seq.get(index);
    }
    public void remove(Item item, int index){
        seq.remove(index);
        size--;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Item item : this)
            s.append(item + " ");
        return s.toString();
    }

    public Iterator<Item> iterator() {
        return new SeqIterator();
    }

    private class SeqIterator implements Iterator<Item> {
        private int index = 0;
        public boolean hasNext()  { return !(size == 0);                    }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = seq.get(index);
            index++;
            return item;
        }
    }
}
