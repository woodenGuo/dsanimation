package com.dsani.dsanimation.algs.visualizer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.provider.Telephony;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsani.dsanimation.R;
import com.dsani.dsanimation.algs.AlgorithmVisualizer;
import com.dsani.dsanimation.algs.logic.SeqResizeQueue;

import java.util.Random;

import static com.dsani.dsanimation.algs.AlgorithmVisualizer.OPERATION.DELETE;
import static com.dsani.dsanimation.algs.AlgorithmVisualizer.OPERATION.EX;

public class QueueVisualizer extends Drawable implements AlgorithmVisualizer<Integer> {
    private SeqResizeQueue<Integer> q;
    private final SeqResizeQueue<Rect> mRects;
    private OPERATION mOps;

    private Context mContext;
    private Paint mPaintRect;
    private Paint mPaintRectFill;

    private Paint mPaintText;
    private Paint mPaintTextNote;

    private Rect mOpsRect;
    private int mOpsRectInitRight;
    private int mOpsVal;
    private int mDequeueCnt;
    private final int CANVAS_WIDTH;
    private final int MAX_SIZE_IN_CANVAS;

    //based right side
    private final int Q_MOVE_START_X;
    private final int Q_MOVE_END_X;

    private final int MAX_ELEMENT_IN_Q;

    //
    private final int Q_ROAD_LENGTH;
    private final int Q_ROAD_HEIGHT;

    //X coordinate of first element right side (plus GAP) => q road right coordinate
    private final int Q_END_X;

    private String mTextNote;
    private ImageView mImgView;
    ValueAnimator animator, animatorD;



    public QueueVisualizer(Context context, int windowWidth, ImageView imageView){
        mContext = context;
        mRects = new SeqResizeQueue<>();
        q = new SeqResizeQueue<>();
        CANVAS_WIDTH = (int) (windowWidth - 2 * context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        Q_ROAD_HEIGHT = (int) (1.3 * RECT_Y_OFFSET);
        MAX_SIZE_IN_CANVAS = (CANVAS_WIDTH - ELEMENT_GAP_OFFSET)/(ELEMENT_GAP_OFFSET + RECT_X_OFFSET);
        Q_MOVE_END_X = CANVAS_WIDTH + RECT_X_OFFSET;
        Q_MOVE_START_X = 0 ;
        MAX_ELEMENT_IN_Q = MAX_SIZE_IN_CANVAS - 2;
        Q_ROAD_LENGTH = MAX_ELEMENT_IN_Q * (ELEMENT_GAP_OFFSET + RECT_X_OFFSET) + ELEMENT_GAP_OFFSET;
        Q_END_X = Q_ROAD_LENGTH + (CANVAS_WIDTH - Q_ROAD_LENGTH) / 2;
        mContext = context;
        mImgView = imageView;
        mDequeueCnt = 0;

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

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(ANIM_DURATION);
        animatorD = ValueAnimator.ofFloat(0f, 1f);
        animatorD.setDuration(ANIM_DURATION);
        setText();
    }

    @Override
    public void setup(OPERATION ops, Bundle bundle) {
        mOps = ops;
        int deltaX;
        switch (ops){
            case EX:
               //Do nothing, layout already supplied picture
                mImgView.setImageDrawable(this);
                break;
            case INSERT:
                deltaX = Q_END_X - ELEMENT_GAP_OFFSET - mRects.size() * (RECT_X_OFFSET + ELEMENT_GAP_OFFSET);
                try {
                    insert();
                    update(deltaX, 0);
                }catch (Exception e){
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE:
                deltaX = Q_MOVE_END_X - Q_END_X + 2 * ELEMENT_GAP_OFFSET + mDequeueCnt * (RECT_X_OFFSET + ELEMENT_GAP_OFFSET);
                try {
                    delete();
                    update(deltaX, 0);
                }catch (Exception e){
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    /**
     * deltaX < 0 => dequeue, > 0 = > enqueue
     * @called in update,
     */
    @Override
    public void setElement(float deltaX, float deltaY, int index, float valAnimator) {
        int first = mRects.first();
        int length = mRects.sizeofArray();

        // dequeue: element not in queue now, draw deleted node independently
        if (mOps == OPERATION.DELETE){
            mOpsRect.right = (int) (mOpsRectInitRight  + valAnimator * deltaX);
            mOpsRect.left = mOpsRect.right - RECT_X_OFFSET;
        }
        //enqueue: compute from start point
        if ( mOps == OPERATION.INSERT ) {
            int right = (int) (Q_MOVE_START_X + deltaX * valAnimator);
            int left = right - RECT_X_OFFSET;
            int top = VIEW_GAP_OFFSET + (Q_ROAD_HEIGHT - RECT_Y_OFFSET) / 2;
            int bottom = top + RECT_Y_OFFSET;
            mRects.set((first + mRects.size() - 1) % length, new Rect(left, top, right, bottom));
        }
        //draw others
        for(int i = 0; i < mRects.size(); i++){
            //case Insert, already set, so continue;
            int deleteBias = 0;
            if(mOps == OPERATION.INSERT && i == mRects.size() - 1)  continue;
            if (mOps == OPERATION.INSERT)  {
                deltaX = 0;
                mDequeueCnt = 0;
            }
            //case delete, other stay at old position
            if(mOps == OPERATION.DELETE) {
                deltaX = 0;
                deleteBias = -(RECT_X_OFFSET + ELEMENT_GAP_OFFSET);
            }
            if(mOps == EX) deleteBias = -(RECT_X_OFFSET + ELEMENT_GAP_OFFSET);

            //draw other element,
            int right = (int) (Q_END_X -ELEMENT_GAP_OFFSET- i * (RECT_X_OFFSET + ELEMENT_GAP_OFFSET) + valAnimator * deltaX + mDequeueCnt * deleteBias);
            int left = right - RECT_X_OFFSET;
            int top = VIEW_GAP_OFFSET + (Q_ROAD_HEIGHT - RECT_Y_OFFSET) / 2;;
            int bottom = top + RECT_Y_OFFSET;
            mRects.set((first + i) % length , new Rect(left, top, right, bottom));
           // if (i == 0 )Log.d("queue", "right" + right);
        }
    }

    /**
     * if @mOps == INSERT, after insert, all element translation @RECT_OFFSET_X in x direction
     * @param distanceX
     * @param distanceY
     */
    @Override
    public void update(float distanceX, float distanceY) {
        animator.removeAllListeners();
        animatorD.removeAllListeners();
        animator.addUpdateListener(animation -> {
            float valAnimate = (float) animator.getAnimatedValue();
            setElement(distanceX, distanceY, -1, valAnimate);
            setText();
            mImgView.invalidate();
        });

        animatorD.addUpdateListener(animation -> {
            float valAnimate = (float) animatorD.getAnimatedValue();
            setElement(mDequeueCnt * (ELEMENT_GAP_OFFSET + RECT_X_OFFSET), 0, -1, valAnimate);
            setText();
            mImgView.invalidate();
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(mOps == OPERATION.DELETE)  {
                    mOps = EX;
                    animatorD.start();
                }
            }
        });

        animatorD.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mDequeueCnt = 0;
            }
        });

        animator.start();

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int first = mRects.first();
        int length = mRects.sizeofArray();
        //draw Queue Road
        canvas.save();
        canvas.translate((float) CANVAS_WIDTH / 2, 0);
        canvas.drawLine((float) -Q_ROAD_LENGTH/2, VIEW_GAP_OFFSET,
                (float) Q_ROAD_LENGTH/2, VIEW_GAP_OFFSET, mPaintText);
        canvas.drawLine((float) -Q_ROAD_LENGTH/2, (float) (VIEW_GAP_OFFSET + 1.3 * RECT_Y_OFFSET),
                (float) Q_ROAD_LENGTH/2, (float) (VIEW_GAP_OFFSET + Q_ROAD_HEIGHT), mPaintText);
        canvas.restore();

        //draw dequeue element
        if (mOps == OPERATION.DELETE) {
            canvas.drawRect(mOpsRect, mPaintRectFill);
            canvas.drawRect(mOpsRect, mPaintRect);
            canvas.drawText(String.valueOf(mOpsVal), mOpsRect.centerX(),
                    mOpsRect.exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
        }

        if (q.isEmpty()) {
            mTextNote = "空空如也";
            canvas.drawText(mTextNote, CANVAS_WIDTH / 2, (float) (VIEW_GAP_OFFSET + 1.8 * RECT_Y_OFFSET), mPaintTextNote);
            return;
        }

        //draw element in Queue
        for(int i = 0; i < mRects.size(); i++){
            canvas.drawRect(mRects.get((first + i) % length), mPaintRectFill);
            canvas.drawRect(mRects.get((first + i) % length), mPaintRect);
            canvas.drawText(q.get((first + i) % length).toString(), mRects.get((first + i) % length).centerX(),
                    mRects.get((first + i) % length).exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
        }
        canvas.drawText(mTextNote, CANVAS_WIDTH / 2, (float) (VIEW_GAP_OFFSET + 1.8 * RECT_Y_OFFSET), mPaintTextNote);
    }

    @Override
    public void insert() {
        if(q.size() == MAX_ELEMENT_IN_Q ) throw new ArrayIndexOutOfBoundsException("queue cannot hold more");
        q.enqueue(new Random().nextInt(50));
        mRects.enqueue(new Rect(0, 0, 0, 0));
    }

    @Override
    public void delete() {
        if(q.isEmpty()) throw new ArrayIndexOutOfBoundsException("queue underflow");
        mOpsVal = q.dequeue();
        mOpsRect = mRects.dequeue();
        mOpsRectInitRight = mOpsRect.right;
        mDequeueCnt++;

    }

    @Override
    public void setText() {
        mTextNote = "具有" + String.valueOf(q.size()) + "个元素的顺序队列";
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
