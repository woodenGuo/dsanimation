package com.dsani.dsanimation.algs.visualizer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.dsani.dsanimation.DataUtil;
import com.dsani.dsanimation.R;
import com.dsani.dsanimation.algs.AlgorithmVisualizer;
import com.dsani.dsanimation.algs.logic.SeqList;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;


public class SeqListDrawable extends Drawable implements AlgorithmVisualizer<Integer> {

    public  static  String Tag = "Sqlist";

    private Context mContext;
    private int CANVAS_WIDTH;
    private static final int TEXT_SIZE = 50;
    private static final int RECT_X_OFFSET = 100; //element rect length
    private static final int RECT_Y_OFFSET = 100;
    private static final int VIEW_GAP_OFFSET = 70;//gap to view hold this seqlistview
    private int mInitElementSize;

    private float mCDElementX, mCDElementY; //from(C)/to(D) Y coordinator of new/deleted element
    private int mOpsIdx;
    private static final int SEQ_ELEMNT_GAP_OFFSET = 5; //element gap
    private SeqList mSeqList;
    private OPERATION mOps;
    private int startX; //init coordinator x of seq
    private ArrayList<Rect> mRects;
    private Pair<Rect, Integer> mOpsNode;
    private int mOpsNodeVal;

    private Paint mPaintRect;
    private Paint mPaintRectFill;

    private Paint mPaintText;
    private Paint mPaintTextNote;


    private Paint mPaintCD;
    private ValueAnimator mCDAnimator;
    private String mTextNote;
    private Paint mPainPath;
    private Path mPathSeq;

    private ValueAnimator mAnimator;
    private ImageView mImgView;

    public SeqListDrawable(Context context, int windowWidth,
                           ImageView imgView){

        mCDElementY = 1.8f * (float) RECT_Y_OFFSET;
        mCDElementX = RECT_X_OFFSET;
        mOpsIdx = -1;
        CANVAS_WIDTH = (int) (windowWidth - 2 * context.getResources().getDimension(R.dimen.activity_horizontal_margin));
        mContext = context;
        mImgView = imgView;
        //init seqlist
        mSeqList = new SeqList<Integer>();
        mRects = new ArrayList<Rect>();
        mInitElementSize = new Random().nextInt(ELEMENT_MAX_SIZE - ELEMENT_MIN_SIZE) + ELEMENT_MIN_SIZE;
        for (int i = 0; i < mInitElementSize; i++) {
            mSeqList.insert(new Random().nextInt(50), i);
            mRects.add(i, new Rect(0, 0, 0, 0));
        }
        startX = (CANVAS_WIDTH -  mSeqList.size() * RECT_X_OFFSET) / 2;
        init();

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(2500);
        mCDAnimator = ObjectAnimator.ofInt( mPaintCD, "color", (0x88<<24) | mContext.getResources().getColor(R.color.rectgrayfill),
                (0x88<<24) | mContext.getResources().getColor(R.color.dsblue),
                (0x88<<24) | mContext.getResources().getColor(R.color.rectgrayfill));
        mCDAnimator.setEvaluator(new DataUtil.ArgbEvaluator());
        mCDAnimator.setDuration(2500);

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

    }

    @Override
    public void setup(OPERATION ops) {
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
            default:
                break;

        }
    }

    @Override
    public void setup(OPERATION ops, int idx, Integer val){
        mOpsNodeVal = val;
        mOps = ops;
        if (idx < 0) mOpsIdx = 0;
        else if (idx > mSeqList.size()) mOpsIdx = mSeqList.size();
        else mOpsIdx = idx;
        switch (ops){
            case GET:
                get(mOpsIdx);
                break;
            case INSERT:
                break;
            default:
                break;

        }
//        ValueAnimator textSizeAnimator = ObjectAnimator.ofFloat(mPaintText, "textSize", TEXT_SIZE, (float) (1.5 * TEXT_SIZE));
//        ValueAnimator textColorAnimator = ObjectAnimator.ofInt( mPaintText, "color", (0xFF<<24) | mContext.getResources().getColor(R.color.black),
//                                                                             (0x00<<24) | mContext.getResources().getColor(R.color.black));
//        ValueAnimator newTextSizeAnimator = ObjectAnimator.ofFloat(mPaintText, "textSize",  (float) (1.5 * TEXT_SIZE), TEXT_SIZE, TEXT_SIZE);
//        ValueAnimator newTextColorAnimator = ObjectAnimator.ofInt( mPaintText, "color", (0x00<<24) | mContext.getResources().getColor(R.color.black),
//                                                                     (0xFF<<24) | mContext.getResources().getColor(R.color.black));
//
//        textColorAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                set(mOpsIdx, mOpsNodeVal);
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.play(newTextSizeAnimator).with(newTextColorAnimator);
//
//            }
//        });
//
//        textSizeAnimator.addUpdateListener(animation -> {
//            mImgView.invalidate();
//        });
//
//        newTextSizeAnimator.addUpdateListener(animation -> {
//            mImgView.invalidate();
//        });
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(textSizeAnimator).with(textColorAnimator);

    }

    @Override
    public void update(){
    }

    @Override
    public void update(float distanceX, float distanceY){
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
     * @note. maybe other algs/ds needs different args to realize the animation
     *        so call this one in interface @setElement()
     * @param: index. cd element, if index is -1, means no element to be CD.
     * @param deltaX, end point - begint point, for element which is After CD element.
     *               '+' => right forward => C
     *               '-' => left forward => D
     *        deltaY, for CD element
     *               '-' => up => D
     *                '+' => down => C
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
            mOpsNode = new Pair<>(mRects.get(mOpsIdx), (Integer)mSeqList.get(mOpsIdx));
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mOps == OPERATION.GET) {
            canvas.drawText(mSeqList.get(mOpsIdx).toString(), mRects.get(mOpsIdx).centerX(),
                    mRects.get(mOpsIdx).exactCenterY() - (mPaintText.getFontMetrics().top + mPaintText.getFontMetrics().bottom)/2, mPaintText);
            return;
        }

        if (mOpsNode != null && mOps == OPERATION.DELETE){
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
        if (mSeqList.size() == 0) mTextNote = "空空如也";
        canvas.drawText(mTextNote, CANVAS_WIDTH / 2, (float) (VIEW_GAP_OFFSET + 1.8 * RECT_Y_OFFSET), mPaintTextNote);
    }

    @Override
    public void setElement(){

    }

    @Override
    public void setPath(){

    }

    @Override
    public void setText() {
        mTextNote = "具有" + String.valueOf(mSeqList.size()) + "个元素的顺序表";
    }

    @Override
    public void insert() {
        if(mSeqList.isEmpty()){
            mSeqList.add(new Random().nextInt(50));
            mRects.add(0, new Rect(0,0,0,0));

            mOpsIdx = 0;
            return;
        }
        mOpsIdx = new Random().nextInt( mSeqList.size());
        if((mRects.get(0).left < 50 && mOpsIdx == 0 ) || (mRects.get(mRects.size() - 1).right + 50 > CANVAS_WIDTH && mOpsIdx != 0) ){
            mOpsIdx = -1;
            Toast.makeText(mContext, "screen overflow", Toast.LENGTH_SHORT).show();
            return;
        }
        mSeqList.insert(new Random().nextInt(50), mOpsIdx);
        mOpsNodeVal = (Integer) mSeqList.get(mOpsIdx);
        mRects.add(mOpsIdx, new Rect(0, 0, 0, 0));

    }


    @Override
    public void delete() {
        if(mSeqList.isEmpty()){
            Toast.makeText(mContext, "seqlist under flow", Toast.LENGTH_SHORT).show();
            mOpsIdx = -1;
            return;
        }
        mOpsIdx = new Random().nextInt(mSeqList.size() - 1);
        if (mOpsIdx == 0) mOpsIdx = 1; //except head and tail

        mOpsNodeVal = (Integer) mSeqList.get(mOpsIdx);
        mSeqList.remove(mOpsIdx);
        mRects.remove(mOpsIdx);

    }


    @Override
    public void get(int index) {
        if(mSeqList.isEmpty()){
            Toast.makeText(mContext, "seqlist under flow", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(mContext, "第" + index + "个元素是" + mSeqList.get(index), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void set(int index, Integer val) {

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
