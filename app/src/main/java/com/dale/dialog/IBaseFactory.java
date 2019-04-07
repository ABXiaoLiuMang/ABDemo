package com.dale.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 文件描述:
 * 作者Dale:2019/4/5
 */
public class IBaseFactory implements BaseFactory {

    private Context context;

    public IBaseFactory(Context context){
        this.context = context;
    }

    @Override
    public View crateTitle() {
        TextView titleView = new TextView(context);
        titleView.setText("title");
        return titleView;
    }

    @Override
    public View createContent() {
        TextView textView = new TextView(context);
        textView.setText("message");
        return textView;
    }

    @Override
    public View createButtonViews() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(60,20,60,20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        Button button = new Button(context);
        button.setText("ok");
        linearLayout.addView(button,lp);

        return linearLayout;
    }
}
