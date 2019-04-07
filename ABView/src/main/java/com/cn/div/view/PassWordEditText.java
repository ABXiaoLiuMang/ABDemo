package com.cn.div.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/12/22.
 */

public class PassWordEditText extends android.support.v7.widget.AppCompatEditText {
    //切换drawable的引用
    private Drawable visibilityDrawable;

    private boolean visibililty = false;
    private boolean isClose = true;

    public void closeSeePass(boolean isClose) {
        this.isClose = isClose;
    }

    public PassWordEditText(Context context) {
        this(context, null);
    }

    public PassWordEditText(Context context, AttributeSet attrs) {
        //指定了默认的style属性
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PassWordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //获得该EditText的left ,top ,right,bottom四个方向的drawable
        Drawable[] compoundDrawables = getCompoundDrawables();
        visibilityDrawable = compoundDrawables[2];
        if (visibilityDrawable == null) {
            visibilityDrawable = getResources().getDrawable(R.mipmap.ico_display_off);
        }

        setEditTextDrawable();
        this.setTransformationMethod(PasswordTransformationMethod.getInstance());
        addTextChangedListener(new TextWatcher() { // 对文本内容改变进行监听
            @Override
            public void afterTextChanged(Editable paramEditable) {
            }

            @Override
            public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
            }

            @Override
            public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
                PassWordEditText.this.setEditTextDrawable();
            }
        });
    }

    /**
     * 用按下的位置来模拟点击事件
     * 当按下的点的位置 在  EditText的宽度 - (图标到控件右边的间距 + 图标的宽度)  和
     * EditText的宽度 - 图标到控件右边的间距 之间就模拟点击事件，
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            if (!isClose) {
                if (getCompoundDrawables()[2] != null) {
                    boolean xFlag = false;
                    boolean yFlag = false;
                    //得到用户的点击位置，模拟点击事件
                    xFlag = event.getX() > getWidth() - (visibilityDrawable.getIntrinsicWidth() + getCompoundPaddingRight
                            ()) &&
                            event.getX() < getWidth() - (getTotalPaddingRight() - getCompoundPaddingRight());

                    if (xFlag) {
                        visibililty = !visibililty;
                        if (visibililty) {
                            visibilityDrawable = getResources().getDrawable(R.mipmap.ico_display_on);
                            this.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            //隐藏密码
                            visibilityDrawable = getResources().getDrawable(R.mipmap.ico_display_off);
                            this.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }

                        //将光标定位到指定的位置
                        CharSequence text = this.getText();
                        if (text instanceof Spannable) {
                            Spannable spanText = (Spannable) text;
                            Selection.setSelection(spanText, text.length());
                        }
                        //调用setCompoundDrawables方法时，必须要为drawable指定大小，不然不会显示在界面上
                        visibilityDrawable.setBounds(0, 0, visibilityDrawable.getMinimumWidth(),
                                visibilityDrawable.getMinimumHeight());
                        setCompoundDrawables(getCompoundDrawables()[0],
                                getCompoundDrawables()[1], visibilityDrawable, getCompoundDrawables()[3]);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setCompoundDrawables(Drawable paramDrawable1, Drawable paramDrawable2, Drawable paramDrawable3, Drawable paramDrawable4) {
        if (paramDrawable3 != null) {
            this.visibilityDrawable = paramDrawable3;
        }
        super.setCompoundDrawables(paramDrawable1, paramDrawable2, paramDrawable3, paramDrawable4);
    }

    // 控制图片的显示
    public void setEditTextDrawable() {
        if (getText().toString().length() == 0) {
            setCompoundDrawables(null, null, null, null);
        } else {
            setCompoundDrawables(null, null, this.visibilityDrawable, null);
        }
    }
}
