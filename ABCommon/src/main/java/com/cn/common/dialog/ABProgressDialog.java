package com.cn.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cn.common.ui.R;


/**
 * 加载等待对话框
 */

public class ABProgressDialog extends Dialog {

    View _view;

    public ABProgressDialog(Context context){
        super(context, R.style.MyDialogStyle);
        _view = LayoutInflater.from(context).inflate(
                R.layout.load_dialog, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(_view);
    }

}

