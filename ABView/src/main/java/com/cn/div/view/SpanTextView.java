package com.cn.div.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


/**
 * @author : Andrew
 * @createTime ：2019/3/10
 * @function description :
 */
public class SpanTextView extends android.support.v7.widget.AppCompatTextView {

    private final Paint subPaint;
    private final Paint mainPaint;
    private final Rect f;
    private String mainTitle;
    private String subTile;
    private int mainTitleColor;
    private int subTileColor;
    private float mainTitleSize;
    private float subTileSize;
    private float space;
    private float subTitleWidth;

    public SpanTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SpanTextView);

        mainTitle = array.getString(R.styleable.SpanTextView_main_title);
        subTile = array.getString(R.styleable.SpanTextView_sub_title);
        mainTitleColor = array.getColor(R.styleable.SpanTextView_main_title_color, Color.GRAY);
        subTileColor = array.getColor(R.styleable.SpanTextView_sub_title_color, Color.GRAY);
        mainTitleSize = array.getDimension(R.styleable.SpanTextView_main_title_size,0f);
        subTileSize = array.getDimension(R.styleable.SpanTextView_sub_title_size,0f);
        space = array.getDimension(R.styleable.SpanTextView_space,5);

        array.recycle();

        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setTextSize(mainTitleSize);
        mainPaint.setColor(mainTitleColor);
        subPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        subPaint.setTextSize(subTileSize);
        subPaint.setColor(subTileColor);

        f = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width , height;
        subTitleWidth = subPaint.measureText(subTile);
        mainPaint.getTextBounds(mainTitle,0,mainTitle.length(),f);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = (int) (subTitleWidth+f.right-f.left+space);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = f.bottom-f.top+4;
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredWidth = getMeasuredWidth();

        subTitleWidth = subPaint.measureText(subTile);
        mainPaint.getTextBounds(mainTitle,0,mainTitle.length(),f);

        float startX = (measuredWidth - space - subTitleWidth - (f.right - f.left)) / 2;

        canvas.drawText(mainTitle,0,mainTitle.length(),startX,f.bottom - f.top-2,mainPaint);
        canvas.drawText(subTile,0,subTile.length(),startX + f.right-f.left+space,f.bottom - f.top-2,subPaint);
    }
    
    public void setTypefaces(Typeface main,Typeface sub) {
        mainPaint.setTypeface(main);
        subPaint.setTypeface(sub);
        requestLayout();
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
        requestLayout();
    }
}
