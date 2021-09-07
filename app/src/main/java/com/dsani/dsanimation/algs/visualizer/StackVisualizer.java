package com.dsani.dsanimation.algs.visualizer;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsani.dsanimation.algs.AlgorithmVisualizer;

public class StackVisualizer  extends Drawable implements AlgorithmVisualizer<Integer> {


    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setup(OPERATION ops, Bundle bundle) {
        switch (ops){
            case EX:

                break;
            case INSERT:
                insert();

                break;
            case DELETE: // override the val, so distancY = 0
                delete();

                break;
            case GET:

                break;
            case SET:
                update();
                break;
            default:
                break;

        }
    }

    @Override
    public void setElement(float deltaX, float deltaY, int index, float valAnimator) {

    }

    @Override
    public void setText() {

    }

    @Override
    public void insert() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void get(int index) {

    }

    @Override
    public void set(int index, Integer integer) {

    }

    @Override
    public void update() {

    }

    @Override
    public void update(float disanceX, float distanceY) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
