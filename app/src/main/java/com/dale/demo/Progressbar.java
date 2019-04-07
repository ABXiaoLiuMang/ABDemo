package com.dale.demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 文件描述:
 * 作者Dale:2019/3/18
 */
public class Progressbar extends View {

    private Paint paint = new Paint(); // 绘制背景灰色线条画笔
    private Paint paintText = new Paint(); // 绘制下载进度画笔
    private float maxvalue = 0f; // 总大小
    private float currentValue = 0f; //
    private Rect mBound = new Rect(); // 获取百分比数字的长宽
    private String percentValue = "0%"; // 要显示的现在百分比


    private int textSize; // 百分比的文字大小
    private int barHeight; // 进度条高度
    private int padding; // 进度条内边界
    private int bgColor; // 背景颜色
    private int proColor; // 进度颜色

    public Progressbar(Context context) {
        this(context, null);
    }

    public Progressbar(Context context, AttributeSet attribute) {
        this(context, attribute, 0);
    }

    public Progressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        textSize = (int) t.getDimension(R.styleable.ProgressBar_textsize, 36);
        barHeight = (int) t.getDimension(R.styleable.ProgressBar_barHeight, 28);
        padding = (int) t.getDimension(R.styleable.ProgressBar_padding, 5);
        bgColor = t.getColor(R.styleable.ProgressBar_bgColor, context.getResources().getColor(R.color.colorPrimary));
        proColor = t.getColor(R.styleable.ProgressBar_proColor, context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制底色
        paint.setStrokeWidth(barHeight);
        paint.setColor(bgColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(0, barHeight / 2, getWidth() , barHeight / 2, paint);
        canvas.drawLine(getLeft(), barHeight+30 , getWidth() , barHeight+30 , paint);
        paint.setColor(proColor);
        paint.setStrokeWidth(barHeight - padding * 2);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Log.d("Dream","w:"+getWidth()+"  l:"+ getLeft()+"  r:" +getRight() +" currentValue:"+currentValue+"  maxvalue:"+maxvalue);
        if(currentValue>0){
            canvas.drawLine(getLeft() + padding/2, barHeight/2  , (getWidth()-getLeft()-getRight()) * currentValue/maxvalue  + getLeft()-padding/2, barHeight/2 , paint);
//            canvas.drawLine(getLeft() + padding/2, barHeight/2  , getWidth() * currentValue/maxvalue  + getLeft()-padding/2, barHeight/2 , paint);
        }
//        paint.setStrokeWidth(1);
//        paintText.setColor(getResources().getColor(R.color.colorPrimaryDark));
//        paintText.setTextSize(textSize);
//        paintText.setAntiAlias(true);
//        paintText.getTextBounds(percentValue, 0, percentValue.length(), mBound);
//        canvas.drawText(percentValue, getWidth() * currentValue/maxvalue + getLeft() - (mBound.width() / 2), barHeight + mBound.height() + 10, paintText);
    }

    public void setMaxValue(float maxValue) {
        this.maxvalue = maxValue;
    }


    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
        int value = (int) (this.currentValue / maxvalue * 100);
        if (value < 100) {
            percentValue = value + "%";
        } else {
            percentValue = "100%";
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.d("Dream","widthSpecSize:"+widthSpecSize);
        int height = barHeight + 60;
        setMeasuredDimension(widthSpecSize, height);
    }


}
