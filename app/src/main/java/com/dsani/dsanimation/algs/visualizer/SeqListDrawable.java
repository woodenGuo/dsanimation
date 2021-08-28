package com.dsani.dsanimation.algs.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dsani.dsanimation.R;
import com.dsani.dsanimation.algs.logic.SeqList;

import static android.graphics.Typeface.DEFAULT;

public class SeqListDrawable extends Drawable {

    private Context mContext;
    private int CANVAS_SIZE;
    private static final int TEXT_SIZE = 50;
    private static final int RECT_X_OFFSET = 100; //element rect length
    private static final int RECT_Y_OFFSET = 100;
    private static final int VIEW_GAP_OFFSET = 50;//gap to view hold this seqlistview
    private static final int SEQ_ELEMENT_CNT = 6;// element cnt
    private static final int SEQ_ELEMNT_GAP_OFFSET = 5; //element gap
    private SeqList mSeqList;
    private String EXDesc;
    private Rect[] mRects;
    private Paint mPaintRect;
    private Paint mPaintRectFill;
    private Paint mPaintText;
    private Paint mPaintTextNote;
    private Paint mPainPath;
    private Path mPathSeq;

    public SeqListDrawable(Context context, int canvasWidth){

        CANVAS_SIZE = canvasWidth;
        mSeqList = new SeqList<String>();
        String EXDesc;
        mContext = context;
        for (int i = 0; i < SeqListDrawable.SEQ_ELEMENT_CNT; i++) {
            mSeqList.insert("a" + String.valueOf(i), i);
        }
        mRects = new Rect[mSeqList.size()];
        mPaintRect = new Paint();
        mPaintRect.setColor(context.getResources().getColor(R.color.black));
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setAntiAlias(true);

        mPaintRectFill = new Paint();
        mPaintRectFill.setColor(context.getResources().getColor(R.color.rectgrayfill));
        mPaintRectFill.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        mPaintText.setTextSize(TEXT_SIZE);
        mPaintText.setColor(context.getResources().getColor(R.color.black));
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mPaintTextNote = new Paint();
        mPaintTextNote.setTextSize(TEXT_SIZE);
        mPaintTextNote.setColor(context.getResources().getColor(R.color.black));
        mPaintTextNote.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        mPaintTextNote.setAntiAlias(true);
        mPaintTextNote.setTextAlign(Paint.Align.CENTER);

    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        int seqListSize = mSeqList.size();
        int seqDrawLength = seqListSize * RECT_X_OFFSET;
        //mid line delta between CANVAS and SEQLENGTH
        int delta = (CANVAS_SIZE - seqDrawLength)/2;
        EXDesc = "具有" + String.valueOf(seqListSize) + "个元素的线性表";
        for(int i = 0; i < mSeqList.size(); i++){

            int left = delta + RECT_X_OFFSET * i;
            int right = left + RECT_X_OFFSET;
            int top = VIEW_GAP_OFFSET;
            int bottom = top + RECT_Y_OFFSET;

            mRects[i] = new Rect(left, top, right, bottom);
            canvas.drawRect(mRects[i], mPaintRectFill);
            canvas.drawRect(mRects[i], mPaintRect);

            Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
            float textTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float textBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            float baseLineY = mRects[i].centerY() - textTop/2 - textBottom/2;
            canvas.drawText((String) mSeqList.get(i), mRects[i].centerX(), baseLineY, mPaintText);
            canvas.drawText(EXDesc, CANVAS_SIZE/2, baseLineY + RECT_Y_OFFSET , mPaintTextNote);
        }

    }

    public void drawDef( Canvas canvas){

    }

    public void drawInsert(){

    }

    public void drawDelete(){

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
