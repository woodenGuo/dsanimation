package com.dsani.dsanimation.algs.visualizer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsani.dsanimation.R;
import com.dsani.dsanimation.algs.AlgorithmVisualizer;
import com.dsani.dsanimation.algs.logic.Queue;

import java.util.ArrayList;

public class QueueVisualizer extends Drawable implements AlgorithmVisualizer<Integer> {
    private Queue<Integer> q;
    private final ArrayList<Rect> mRects;
    private OPERATION mOps;

    private Context mContext;
    private Paint mPaintRect;
    private Paint mPaintRectFill;

    private Paint mPaintText;
    private Paint mPaintTextNote;

    private int vel;
    private final int CANVAS_WIDTH;

    private ValueAnimator mAnimator;
    private ImageView mImgView;

    public QueueVisualizer(Context context, int windowWidth, ImageView imageView){
        mContext = context;
        mRects = new ArrayList<>();
        q = new Queue<>();
        CANVAS_WIDTH = (int) (windowWidth - 2 * context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        mContext = context;
        mImgView = imageView;

        //init var associated with CANVAS
        mPaintRect = new Paint();
        mPaintRect.setColor(mContext.getResources().getColor(R.color.black));
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setAntiAlias(true);

        mPaintRectFill = new Paint();
        mPaintRectFill.setColor(mContext.getResources().getColor(R.color.rectgrayfill));
        mPaintRectFill.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        mPaintText.setTextSize(TEXT_SIZE);
        mPaintText.setColor(mContext.getResources().getColor(R.color.black));
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mPaintTextNote = new Paint();
        mPaintTextNote.setTextSize(TEXT_SIZE);
        mPaintTextNote.setColor(mContext.getResources().getColor(R.color.black));
        mPaintTextNote.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        mPaintTextNote.setAntiAlias(true);
        mPaintTextNote.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float currentDeltaX = 0f;
        float currentDeltaY = 0f;
        int centerBias = (CANVAS_WIDTH - q.size() * RECT_X_OFFSET) / 2;

        for(int i = 0; i < q.size(); i++){

            int left = (int) ((float)(centerBias + RECT_X_OFFSET * i) + currentDeltaX);
            int right = left + RECT_X_OFFSET;
            int top = (int) ((float)VIEW_GAP_OFFSET + currentDeltaY) ;
            int bottom = top + RECT_Y_OFFSET;
            mRects.set(i, new Rect(left, top, right, bottom));
        }

        //set deleted node/not in seqlist, saved in pair
        if (mOps == OPERATION.DELETE )  {

        }else if (mOps == OPERATION.INSERT){

        }
    }

    @Override
    public void setup(OPERATION ops, Bundle bundle) {

        switch (ops){
            case EX:
               //Do nothing, layout already supplied picture
                break;
            case INSERT:
                insert();

                break;
            case DELETE:
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
    public void setPath() {

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
