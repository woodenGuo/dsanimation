package com.dsani.dsanimation.algs.visualizer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.util.Pair;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.dsani.dsanimation.DataUtil;
import com.dsani.dsanimation.R;
import com.dsani.dsanimation.algs.AlgorithmVisualizer;
import com.dsani.dsanimation.algs.logic.SeqList;

import java.util.ArrayList;
import java.util.Random;


public class SeqListVisualizer extends Drawable implements AlgorithmVisualizer<Integer> {

    private final Context mContext;
    private final int CANVAS_WIDTH;
    private final int MAX_SIZE_IN_CANVAS;
    private final float mCDElementX;
    private final float mCDElementY; //from(C)/to(D) Y coordinator of new/deleted element
    private int mOpsIdx;
    private final SeqList<Integer> mSeqList;
    private OPERATION mOps;

    private final ArrayList<Rect> mRects;
    private Pair<Rect, Integer> mOpsNode;
    private int mOpsNodeVal;

    private Paint mPaintRect;
    private Paint mPaintRectFill;

    private Paint mPaintText;
    private Paint mPaintTextNote;

    private Paint mPaintCD;
    private Paint mPaintUR;
    private ValueAnimator mCDAnimator;
    private String mTextNote;

    private ValueAnimator mAnimator;
    private ImageView mImgView;

    public SeqListVisualizer(Context context, int windowWidth, ImageView imgView){
        mCDElementY = 1.8f * (float) RECT_Y_OFFSET;
        mCDElementX = RECT_X_OFFSET;
        mOpsIdx = -1;
        CANVAS_WIDTH = (int) (windowWidth - 2 * context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        MAX_SIZE_IN_CANVAS = CANVAS_WIDTH / RECT_X_OFFSET;
        mContext = context;
        mImgView = imgView;
        //init seqlist
        mSeqList = new SeqList<Integer>();
        mRects = new ArrayList<Rect>();
        int mInitElementSize = new Random().nextInt(ELEMENT_MAX_SIZE - ELEMENT_MIN_SIZE) + ELEMENT_MIN_SIZE;
        for (int i = 0; i < mInitElementSize; i++) {
            mSeqList.insert(new Random().nextInt(50), i);
            mRects.add(i, new Rect(0, 0, 0, 0));
        }
        init();
        setElement( 0f, 0f, -1, 0f);
        setText();
    }

    private void init(){
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

        mPaintCD = new Paint();
        mPaintCD.setColor(mContext.getResources().getColor(R.color.rectgrayfill));
        mPaintCD.setStyle(Paint.Style.FILL);

        mPaintUR = new Paint();
        mPaintUR.setTextSize(TEXT_SIZE);
        mPaintUR.setColor(mContext.getResources().getColor(R.color.black));
        mPaintUR.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        mPaintUR.setAntiAlias(true);
        mPaintUR.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void setup(OPERATION ops, Bundle bundle) {

        if (bundle != null){
            mOpsNodeVal = bundle.getInt("val");
            mOpsIdx = bundle.getInt("index");
        }
        mOps = ops;
        switch (ops){
            case EX:
                mImgView.setImageDrawable(this);
                break;
            case INSERT:
                insert();
                update(mCDElementX, mCDElementY);
                break;
            case DELETE: // override the val, so distancY = 0
                delete();
                update(-mCDElementX, 0f);
                break;
            case GET:
                get(mOpsIdx);
                break;
            case SET:
                update();
                break;
            default:
                break;

        }
    }

    @Override
    public void update(){
        ValueAnimator textSizeAnimator = ObjectAnimator.ofFloat(mPaintUR, "textSize",
                TEXT_SIZE, (float) (1.5 * TEXT_SIZE));
        ValueAnimator textColorAnimator = ObjectAnimator.ofInt(mPaintUR, "color",
                mContext.getResources().getColor(R.color.black), //TODO. R.color.black has som problem
                mContext.getResources().getColor(R.color.rectgrayfill));
        ValueAnimator newTextSizeAnimator = ObjectAnimator.ofFloat(mPaintUR, "textSize",
                (float) (1.5 * TEXT_SIZE), TEXT_SIZE);
        ValueAnimator newTextColorAnimator = ObjectAnimator.ofInt(mPaintUR, "color",
                mContext.getResources().getColor(R.color.rectgrayfill),
                mContext.getResources().getColor(R.color.black));

        textColorAnimator.setEvaluator(new DataUtil.ArgbEvaluator());
        newTextColorAnimator.setEvaluator(new DataUtil.ArgbEvaluator());

        textColorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                set(mOpsIdx, mOpsNodeVal);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(newTextSizeAnimator).with(newTextColorAnimator);
                animatorSet.setDuration(ANIM_DURATION);
                animatorSet.start();
            }
        });

        textColorAnimator.addUpdateListener(animation -> {
            mImgView.invalidate();
        });

        newTextColorAnimator.addUpdateListener(animation -> {
            mImgView.invalidate();
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(textSizeAnimator).with(textColorAnimator);
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.start();
    }

    @Override
    public void update(float distanceX, float distanceY){
        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(ANIM_DURATION);
        mCDAnimator = ObjectAnimator.ofInt( mPaintCD, "color",
                (0x88<<24) | mContext.getResources().getColor(R.color.rectgrayfill),
                (0x88<<24) | mContext.getResources().getColor(R.color.dsblue),
                (0x88<<24) | mContext.getResources().getColor(R.color.rectgrayfill));
        mCDAnimator.setEvaluator(new DataUtil.ArgbEvaluator());
        mCDAnimator.setDuration(ANIM_DURATION);

        mAnimator.addUpdateListener(animation -> {
            float valAnimate = (float) mAnimator.getAnimatedValue();
            setElement(distanceX, distanceY, mOpsIdx, valAnimate);
            setText();
            mImgView.invalidate();
        });
        mAnimator.start();
        mCDAnimator.start();
    }

    /**
     *  adapter? => modify interface on 8/30/2021
     * @note. 1. maybe other algs/ds needs different args to realize the animation
     *        so call this one in interface @setElement()
     *        2. @case DELETE. NODE DELETED BEFORE UPDATE COORD OF THE LIST, COORD WAS SAVED IN PAIR
     * @param: index. cd element, if index is -1, means no element to be CD.
     * @param deltaX, end point - begint point, for element which is After CD element.
     *                '+' => right forward => C       '-' => left forward => D
     *        deltaY,'-' => up => D                  '+' => down => C
     *
     */
    @Override
    public void setElement(float deltaX, float deltaY, int index, float valAnimator){
        float currentDeltaX = 0f;
        float currentDeltaY = 0f;
        int centerBias = (CANVAS_WIDTH - mSeqList.size() * RECT_X_OFFSET) / 2;

        for(int i = 0; i < mSeqList.size(); i++){
            if (i < index && index != -1 ) { // element before CD stay stone
                currentDeltaX = 0;
                currentDeltaY = 0;
            }else if(i == index && index != -1){ //CD element place, move in Y direction
                if (deltaX > 0) currentDeltaX = 0;  //D
                if (deltaX < 0) currentDeltaX = deltaX * valAnimator - deltaX;
                if (deltaY > 0) currentDeltaY = -deltaY + deltaY * valAnimator; //C, start point + delta_x
                if (deltaY == 0) currentDeltaY = 0; //D
            }else if(i > index && index != -1){ //after CD, right forward
                currentDeltaY = 0;
                currentDeltaX = deltaX * valAnimator - deltaX; //C/D, node already insert, so sub it
            }
            int left = (int) ((float)(centerBias + RECT_X_OFFSET * i) + currentDeltaX);
            int right = left + RECT_X_OFFSET;
            int top = (int) ((float)VIEW_GAP_OFFSET + currentDeltaY) ;
            int bottom = top + RECT_Y_OFFSET;
            mRects.set(i, new Rect(left, top, right, bottom));
        }

        //set deleted node/not in seqlist, saved in pair
        if (mOps == OPERATION.DELETE && index != -1)  {
            int left = centerBias + RECT_X_OFFSET * mOpsIdx;
            int right = left + RECT_X_OFFSET;
            int top = VIEW_GAP_OFFSET;
            int bottom = top + RECT_Y_OFFSET;
            Rect rect = new Rect(left, top, right, bottom);
            mOpsNode = new Pair<>(rect, mOpsNodeVal);
        }else if (mOps == OPERATION.INSERT && index != -1){
            mOpsNode = new Pair<>(mRects.get(mOpsIdx), mSeqList.get(mOpsIdx));
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //deleted node not in seq_list, is was saved in <Pair> mOpsNode
        if (mSeqList.size() > 0 && mOpsNode != null && mOps == OPERATION.DELETE){
            canvas.drawRect(mOpsNode.first, mPaintCD);
            canvas.drawRect(mOpsNode.first, mPaintRect);
            canvas.drawText(mOpsNode.second.toString(), mOpsNode.first.centerX(),
                    mOpsNode.first.exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
        }

        for(int i = 0; i < mSeqList.size(); i++){
            canvas.drawRect(mRects.get(i), mPaintRectFill);
            canvas.drawRect(mRects.get(i), mPaintRect);
            canvas.drawText(mSeqList.get(i).toString(), mRects.get(i).centerX(),
                    mRects.get(i).exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
        }

        if (mOpsNode != null && mOps == OPERATION.INSERT) {
            canvas.drawRect(mOpsNode.first, mPaintCD);
            canvas.drawRect(mOpsNode.first, mPaintRect);
            canvas.drawText(mOpsNode.second.toString(), mOpsNode.first.centerX(),
                    mOpsNode.first.exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
        }

        if (mOps == OPERATION.SET) {
            canvas.drawText(mSeqList.get(mOpsIdx).toString(), mRects.get(mOpsIdx).centerX(),
                    mRects.get(mOpsIdx).exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintUR);;
        }

        if (mSeqList.size() == 0) mTextNote = "空空如也";
        canvas.drawText(mTextNote, CANVAS_WIDTH / 2, (float) (VIEW_GAP_OFFSET + 1.8 * RECT_Y_OFFSET), mPaintTextNote);
    }

    @Override
    public void setText() {
        mTextNote = "具有" + String.valueOf(mSeqList.size()) + "个元素的顺序表";
    }

    @Override
    public void insert() {
        if(mSeqList.size() == MAX_SIZE_IN_CANVAS ){
           Toast.makeText(mContext, "canvas cannot hold more", Toast.LENGTH_SHORT).show();
           return;
        }

        if(mSeqList.isEmpty()){
            mSeqList.add(new Random().nextInt(50));
            mRects.add(0, new Rect(0,0,0,0));

            mOpsIdx = 0;
            return;
        }
        mOpsIdx = new Random().nextInt( mSeqList.size());

        mSeqList.insert(new Random().nextInt(50), mOpsIdx);
        mOpsNodeVal = mSeqList.get(mOpsIdx);
        mRects.add(mOpsIdx, new Rect(0, 0, 0, 0));

    }


    @Override
    public void delete() {
        if(mSeqList.isEmpty()){
            Toast.makeText(mContext, "seqlist under flow", Toast.LENGTH_SHORT).show();
            mOpsIdx = -1;
            return;
        }
        mOpsIdx = new Random().nextInt(mSeqList.size());
        mOpsNodeVal = mSeqList.get(mOpsIdx);
        mSeqList.remove(mOpsIdx);
        mRects.remove(mOpsIdx);

    }


    @Override
    public void get(int index) {
        int val;
        try {
            val = mSeqList.get(index);
        }catch (Exception e){
            Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(mContext, "第" + index + "个元素是" + val, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void set(int index, Integer val) {
        try {
            mSeqList.set(val, index);
        }catch (Exception e){
            Toast.makeText(mContext, e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
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

    @Override
    public void setPath(){

    }
}
