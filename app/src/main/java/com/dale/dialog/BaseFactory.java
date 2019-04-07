package com.dale.dialog;

import android.view.View;

/**
 * 文件描述:
 * 作者Dale:2019/4/5
 */
public interface BaseFactory {

    View crateTitle();

    View createContent();

    View createButtonViews();
}
