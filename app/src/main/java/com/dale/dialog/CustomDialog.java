package com.dale.dialog;

import android.app.Activity;
import android.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dale.demo.R;

/**
 * 文件描述:
 * 作者Dale:2019/4/4
 */
public class CustomDialog implements BaseDialog {

    private FrameLayout rootView;
    private LinearLayout contentView;
    private CustomDialog.Builder builder;
    private boolean isShow;


    private CustomDialog(CustomDialog.Builder builder){
        this.builder = builder;
        onCreate();
    }

    private void onCreate(){

        rootView = builder.context.findViewById(android.R.id.content);
        contentView = new LinearLayout(builder.context);
        contentView.setOrientation(LinearLayout.VERTICAL);
        contentView.addView(builder.baseFactory.crateTitle());
        contentView.addView(builder.baseFactory.createContent());
        contentView.addView(builder.baseFactory.createButtonViews());
        contentView.setBackgroundColor(builder.context.getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void showDialog() {
        if(isShow)
            return;
        isShow = true;
        Window window = builder.context.getWindow();
        Display d = window.getWindowManager().getDefaultDisplay(); // 获取屏幕宽、高用
        int width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        rootView.addView(contentView,layoutParams);
    }

    @Override
    public void dismissDialog() {
        isShow = false;
        rootView.removeView(contentView);
    }

    @Override
    public boolean isShowDialog() {
        return isShow;
    }

    public static class Builder {
        public Activity context;
        public BaseFactory baseFactory;

        public Builder(Activity activity) {
            this.context = activity;
            initDefaultView();
        }

        public Builder(Fragment fragment) {
            this.context = fragment.getActivity();
            initDefaultView();
        }

        public Builder(BaseFactory baseFactory) {
            this.baseFactory = baseFactory;
        }

        private void initDefaultView(){
            baseFactory = new IBaseFactory(context);
        }

        public BaseDialog create() {
            BaseDialog dialog = new CustomDialog(this);
            return dialog;
        }

        public BaseDialog show() {
            BaseDialog dialog = this.create();
            dialog.showDialog();
            return dialog;
        }
    }
}
