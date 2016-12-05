package me.zhouzhuo.zzfragmenttabhost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zz on 2016/10/26.
 */

public class ZzMarkView extends View {

    private int markNumber;
    private int markNumberColor;
    private int markNumberSize;
    private int markBgColor;

    private int markWidth;
    private int markHeight;

    private int maxNumber;

    private Paint mNumPaint;
    private Paint mBgPaint;

    public ZzMarkView(Context context) {
        super(context);
        init(context, null);
    }

    public ZzMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZzMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        initPaints(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZzMarkView);
        markNumber = a.getInteger(R.styleable.ZzMarkView_zmv_mark_default_number, 0);
        markBgColor = a.getColor(R.styleable.ZzMarkView_zmv_mark_bg_color, 0xffff0000);
        markNumberColor = a.getColor(R.styleable.ZzMarkView_zmv_mark_text_color, 0xffffffff);
        markNumberSize = a.getDimensionPixelSize(R.styleable.ZzMarkView_zmv_mark_text_size, 20);
        maxNumber = a.getInteger(R.styleable.ZzMarkView_zmv_mark_max_number, 99);
        a.recycle();
    }

    private void initPaints(Context context) {

        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(markBgColor);

        mNumPaint = new Paint();
        mNumPaint.setColor(markNumberColor);
        mNumPaint.setAntiAlias(true);
        mNumPaint.setTextSize(markNumberSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBg(canvas);

        drawText(canvas);

    }

    private void drawBg(Canvas canvas) {
        float numWidth = mNumPaint.measureText(markNumber + "");
        setMinimumWidth((int) (numWidth + 20));
        setMinimumHeight((int) (numWidth + 20));
        int width = getWidth();
        int height = getHeight();
        int min1 = width < height ? width : height;
        Log.e("xxxx", min1 + "," + numWidth);
        RectF rectF = new RectF();

        rectF.left = (width - numWidth - 10) / 2 ;
        rectF.right =( width + numWidth + 10) / 2 ;
        if (height > numWidth + 20) {
            rectF.top = (height - numWidth - 10) / 2;
            rectF.bottom = (height + numWidth + 10) / 2;
            Log.e("xxxxx", rectF.toString());
            canvas.drawCircle(width/2, height/2, height/2-5, mBgPaint);
        } else {
            rectF.top = 10 / 2;
            rectF.bottom = (height + height - 10) / 2;
            canvas.drawRoundRect(rectF, 100, 100, mBgPaint);
        }

    }

    private void drawText(Canvas canvas) {

        String content = markNumber > maxNumber ? maxNumber+"+" : markNumber+ "";
        float numWidth = mNumPaint.measureText(content);
        float numHeight = mNumPaint.descent() + mNumPaint.ascent();
        canvas.drawText(content, (getWidth() - numWidth) / 2, (getHeight() - numHeight) / 2, mNumPaint);

    }

    public int getMarkNumber() {
        return markNumber;
    }

    public void setMarkNumber(int markNumber) {
        this.markNumber = markNumber;
        invalidate();
    }

    public int getMarkNumberColor() {
        return markNumberColor;
    }

    public void setMarkNumberColor(int markNumberColor) {
        this.markNumberColor = markNumberColor;
        mNumPaint.setColor(markNumberColor);
        invalidate();
    }

    public int getMarkNumberSize() {
        return markNumberSize;
    }

    public void setMarkNumberSize(int markNumberSize) {
        this.markNumberSize = markNumberSize;
        mNumPaint.setTextSize(markNumberSize);
        invalidate();
    }

    public int getMarkBgColor() {
        return markBgColor;
    }

    public void setMarkBgColor(int markBgColor) {
        this.markBgColor = markBgColor;
        mBgPaint.setColor(markBgColor);
        invalidate();
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        invalidate();
    }

    public void setMarkWidth(int markWidth) {
        this.markWidth = markWidth;
        getLayoutParams().width = markWidth;
        invalidate();
    }

    public void setMarkHeight(int markHeight) {
        this.markHeight = markHeight;
        getLayoutParams().height = markHeight;
        invalidate();
    }
}
