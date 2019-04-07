package com.cn.div.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author : Andrew
 * @createTime ：2019/3/15
 * @function description :
 */
public class SearchBar extends FrameLayout implements EditText.OnEditorActionListener , TextWatcher {

    private final ImageView mClose;
    private final EditText mInput;

    public SearchBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_seach_bar, null);
        mInput = view.findViewById(R.id.et_search_input);
        mClose = view.findViewById(R.id.iv_search_close);
        TextView cancel = view.findViewById(R.id.tv_search_cancel);

        mInput.setOnEditorActionListener(this);

        mInput.addTextChangedListener(this);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEvent != null) {
                    mEvent.cancelClick();
                }
            }
        });

        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInput.setText("");
                if (mEvent != null) {
                    mEvent.closeClick();
                }
            }
        });

        LayoutParams params = new LayoutParams(-1, -1);
        view.setLayoutParams(params);

        addView(view);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (mEvent != null) {
                mEvent.searchAction(v.getText().toString().trim());
            }
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean empty = TextUtils.isEmpty(s);
        mClose.setVisibility(empty ? INVISIBLE : VISIBLE);
        if (mEvent != null) {
            mEvent.textChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String content() {
        return mInput.getText().toString().trim();
    }

    public void setContent(String content) {
        mInput.setText(TextUtils.isEmpty(content) ? "" : content);
        mInput.setSelection(TextUtils.isEmpty(content) ? 0 : content.length());
    }

    Event mEvent;

    public void bindEvent(SimpleEvent event) {
        this.mEvent = event;
    }

    private interface Event {
        void cancelClick();

        void textChanged(CharSequence s, int start, int before, int count);

        void searchAction(String keyWord);

        void closeClick();
    }

    public static class SimpleEvent implements Event {

        @Override
        public void cancelClick() {

        }

        @Override
        public void textChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void searchAction(String keyWord) {

        }

        @Override
        public void closeClick() {

        }
    }
}
