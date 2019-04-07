package com.cn.div.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 文件描述:
 * 作者Dale:2019/3/6
 */
public class DragView extends FrameLayout{
//    public static final String KEY_FLOATING_X = "KEY_FLOATING_X";
//    public static final String KEY_FLOATING_Y = "KEY_FLOATING_Y";
//    public static final String KEY_DragView= "DragView";
    ViewDragHelper dragHelper;
    View dragView;
    View contentView;

    public DragView(Context context) {
       this(context,null);
    }


    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = findViewById(R.id.contentView);
        dragView = findViewById(R.id.dragView);
        if(contentView == null){
            throw new RuntimeException("内容布局ID必须为contentView");
        }
        if(dragView == null){
            throw new RuntimeException("可拖拽布局ID必须为dragView");
        }
    }

    private void init() {
        dragHelper = ViewDragHelper.create(DragView.this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == dragView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > getHeight() - child.getMeasuredHeight()) {
                    top = getHeight() - child.getMeasuredHeight();
                } else if (top < 0) {
                    top = 0;
                }
                return top;
            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > getWidth() - child.getMeasuredWidth()) {
                    left = getWidth() - child.getMeasuredWidth();
                } else if (left < 0) {
                    left = 0;
                }
                return left;
            }


            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }


            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }


            //  拖拽的 view 位置发生变化时被回调
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                savePosition();
            }


            @Override
            public void onViewDragStateChanged(int state) {
                super.onViewDragStateChanged(state);
                if (state == ViewDragHelper.STATE_SETTLING) {
                    restorePosition();
                }
            }


            // 在释放拖拽的 view 时，会回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == dragView) {
                    float x = dragView.getX();
                    float y = dragView.getY();
                    if (x < (getMeasuredWidth() / 2f - releasedChild.getMeasuredWidth() / 2f)) { // 0-x/2
                        x = 0;
                    } else { // x/2-x
                        x = getMeasuredWidth() - releasedChild.getMeasuredWidth();
                    }
                    savePosition();
                    // 移动到指定位置
                    dragHelper.smoothSlideViewTo(releasedChild, (int) x, (int) y);
                    invalidate();
                }
            }
        });

    }




    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        savePosition();
    }


    /**
     * 保存位置
     */
    void savePosition() {
        float x = dragView.getX();
        float y = dragView.getY();
//        SpUtils.newSp(KEY_DragView,Context.MODE_PRIVATE)
//                .put(KEY_FLOATING_X,x)
//                .put(KEY_FLOATING_Y,y)
//                .apply();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        SpUtils.newSp(KEY_DragView,Context.MODE_PRIVATE)
//                .put(KEY_FLOATING_X,-1f)
//                .put(KEY_FLOATING_Y,-1f)
//                .apply();
    }


    /**
     * 更新位置
     */
    public void restorePosition() {
        // 读取保存的位置
//        float x =  SpUtils.newSp(KEY_DragView,Context.MODE_PRIVATE).getFloat(KEY_FLOATING_X);
//        float y = SpUtils.newSp(KEY_DragView,Context.MODE_PRIVATE).getFloat(KEY_FLOATING_Y);
//        if (x == -1 && y == -1) { // 初始位置
//            x = getMeasuredWidth() - dragView.getMeasuredWidth();
//            y = getMeasuredHeight();
//        }
//        dragView.layout((int) x, (int) y,
//                (int) x + dragView.getMeasuredWidth(), (int) y + dragView.getMeasuredHeight());

    }

    @Override

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            dragHelper.cancel();
            return false;
        }
        return dragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}
