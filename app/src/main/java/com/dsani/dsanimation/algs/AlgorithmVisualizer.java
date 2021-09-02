package com.dsani.dsanimation.algs;

import android.view.View;

public interface AlgorithmVisualizer<Item> {
    int ELEMENT_MAX_SIZE = 8;// element cnt
    int ELEMENT_MIN_SIZE = 4;
    public enum OPERATION {
        EX,
        INSERT, DELETE, GET, SET,
        SORT_INS, SORT_SHL,
        SORT_BUB, SORT_QCK,
        SORT_SEL, SORT_HEAP,
        SORT_MEG,
        PREORDER, INORDER, POSTORDER,
        BFS, DFS,
        HUFFMAN
    }
    public void setup(OPERATION ops);

    public void setup(OPERATION ops, int idx, Item val);

    public void setElement();

    public void setElement(float deltaX, float deltaY, int index, float valAnimator);

    public void setPath();

    public void setText();

    public void insert();

    public void delete();


    public void get(int index);

    public void set(int index, Item item);

    public void update();

    public void update(float disanceX, float distanceY);

}
