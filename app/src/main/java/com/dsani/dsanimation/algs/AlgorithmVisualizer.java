package com.dsani.dsanimation.algs;

import android.os.Bundle;
import android.view.View;

public interface AlgorithmVisualizer<Item> {

    int TEXT_SIZE = 50;
    int RECT_X_OFFSET = 100; //element rect length
    int RECT_Y_OFFSET = 100;
    int VIEW_GAP_OFFSET = 70;//gap to view hold this seqlistview
    int ANIM_DURATION = 2500;
    int ELEMENT_MAX_SIZE = 8;// element cnt
    int ELEMENT_MIN_SIZE = 4;
    int ELEMENT_GAP_OFFSET = 5;

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
    public void setup(OPERATION ops, Bundle bundle);

    /**
     * set element coordinate by @deltaX, @deltaY and @valAnimator,
     * @index element is the element on which is operated.
     */
    public void setElement(float deltaX, float deltaY, int index, float valAnimator);

    public void setPath();

    public void setText();

    public void insert();

    public void delete();

    public void get(int index);

    public void set(int index, Item item);

    /**
     * setup animator
     */
    public void update();

    public void update(float disanceX, float distanceY);

}
