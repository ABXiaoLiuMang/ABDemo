package com.cn.div.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ClipImageView extends android.support.v7.widget.AppCompatImageView {

    public static final int SCALE_TYPE_MATRIX_LEFT_CROP   = 1; // 等比例缩放，当图像超出控件尺寸时，保留左边，其余部分剪切掉。
    public static final int SCALE_TYPE_MATRIX_RIGHT_CROP  = 2; // 等比例缩放，当图像超出控件尺寸时，保留右边，其余部分剪切掉。
    public static final int SCALE_TYPE_MATRIX_CENTER_CROP = 3; // 等比例缩放，当图像超出控件尺寸时，保留中间，其余部分剪切掉。
    private Path path;

    @IntDef({SCALE_TYPE_MATRIX_LEFT_CROP, SCALE_TYPE_MATRIX_RIGHT_CROP, SCALE_TYPE_MATRIX_CENTER_CROP})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ScaleTypeMatrixExt {
    }

    private int mScaleTypeMatrixExt;

    float width, height;
    private int defaultRadius = 0;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    public ClipImageView(Context context) {
        this(context, null);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClipImageView);
        mScaleTypeMatrixExt = typedArray.getInt(R.styleable.ClipImageView_ive_scale_type_matrix_ext, -1);
        radius = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_radius, defaultRadius);
        leftTopRadius = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_left_top_radius, defaultRadius);
        rightTopRadius = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_right_top_radius, defaultRadius);
        rightBottomRadius = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_right_bottom_radius, defaultRadius);
        leftBottomRadius = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_left_bottom_radius, defaultRadius);

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }
        typedArray.recycle();

        path = new Path();
    }

    public void setRadius(int radius) {
        this.radius
                = this.leftTopRadius
                = this.rightTopRadius
                = this.leftBottomRadius
                = this.rightBottomRadius
                = radius;
        requestLayout();
    }

    public void setLeftTopRadius(int leftTopRadius) {
        this.leftTopRadius = leftTopRadius;
        requestLayout();
    }

    public void setRightTopRadius(int rightTopRadius) {
        this.rightTopRadius = rightTopRadius;
        requestLayout();
    }

    public void setRightBottomRadius(int rightBottomRadius) {
        this.rightBottomRadius = rightBottomRadius;
        requestLayout();
    }

    public void setLeftBottomRadius(int leftBottomRadius) {
        this.leftBottomRadius = leftBottomRadius;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //这里做下判断，只有图片的宽高大于设置的圆角距离的时候才进行裁剪
        int maxLeft = Math.max(leftTopRadius, leftBottomRadius);
        int maxRight = Math.max(rightTopRadius, rightBottomRadius);
        int minWidth = maxLeft + maxRight;
        int maxTop = Math.max(leftTopRadius, rightTopRadius);
        int maxBottom = Math.max(leftBottomRadius, rightBottomRadius);
        int minHeight = maxTop + maxBottom;
        if (width >= minWidth && height > minHeight) {
            //四个角：右上，右下，左下，左上
            path.moveTo(leftTopRadius, 0);
            path.lineTo(width - rightTopRadius, 0);
            path.quadTo(width, 0, width, rightTopRadius);

            path.lineTo(width, height - rightBottomRadius);
            path.quadTo(width, height, width - rightBottomRadius, height);

            path.lineTo(leftBottomRadius, height);
            path.quadTo(0, height, 0, height - leftBottomRadius);

            path.lineTo(0, leftTopRadius);
            path.quadTo(0, 0, leftTopRadius, 0);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        handScaleTypeMatrixExt();
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        handScaleTypeMatrixExt();
    }

    private void handScaleTypeMatrixExt() {
        if (getDrawable() == null) {
            return;
        }

        if (this.getScaleType() == ScaleType.MATRIX && mScaleTypeMatrixExt != -1) {
            // 图片实际尺寸
            final int dwidth = getDrawable().getIntrinsicWidth();
            final int dheight = getDrawable().getIntrinsicHeight();
            // ImageView图片显示尺寸
            final int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
            final int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
            float scale;
            float dx = 0, dy = 0;
            if (dwidth * vheight > vwidth * dheight) {      // 图片宽高比 > 控件宽高比
                scale = (float) vheight / (float) dheight;
                switch (mScaleTypeMatrixExt) {
                    case SCALE_TYPE_MATRIX_LEFT_CROP:
                        dx = 0;                             // 保留左边
                        break;
                    case SCALE_TYPE_MATRIX_RIGHT_CROP:
                        dx = (vwidth - dwidth * scale);     // 保留右边
                        break;
                    case SCALE_TYPE_MATRIX_CENTER_CROP:
                        dx = (vwidth - dwidth * scale) * 0.5f; // 保留中间（效果与 CENTER_CROP 一样）
                        break;
                    default:
                        break;
                }
            } else {
                scale = (float) vwidth / (float) dwidth;
                // dy = (vheight - dheight * scale) * 0.5f; // 根据实际情况编写，默认为0，保留上边
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(Math.round(dx), Math.round(dy));
            this.setImageMatrix(matrix);
        }
    }

    public void setScaleTypeMatrixExt(@ScaleTypeMatrixExt int scaleTypeMatrixExt) {
        this.mScaleTypeMatrixExt = scaleTypeMatrixExt;
        requestLayout();
    }
}