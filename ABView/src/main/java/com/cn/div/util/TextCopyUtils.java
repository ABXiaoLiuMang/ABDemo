package com.cn.div.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/4/30.
 */

public class TextCopyUtils {

    public static String getSelectText(Context context, TextView tv, SelectMode mode) {
        //截取选中的文本
        return getSelectText(context, tv.getText().toString(), mode);
    }

    /**
     * 统一处理复制和剪切的操作
     *
     * @param mode 用来区别是复制还是剪切
     * @return
     */
    public static String getSelectText(Context context, String txt, SelectMode mode) {
        return getSelectText(context, txt, mode, true);
    }


    /**
     * 统一处理复制和剪切的操作
     *
     * @param mode 用来区别是复制还是剪切
     * @return
     */
    public static String getSelectText(Context context, String txt, SelectMode mode, boolean showToast) {
        //获取剪切班管理者
        ClipboardManager cbs = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //获取选中的起始位置
        /*int selectionStart = mTvSelect.getSelectionStart();
        int selectionEnd = mTvSelect.getSelectionEnd();*/
        //将选中的文本放到剪切板
        cbs.setPrimaryClip(ClipData.newPlainText(null, txt));
        //如果是复制就不往下操作了

        if (mode == SelectMode.COPY && showToast) {
            return txt;
        }
        //把剪切后的数据替换""
        txt = txt.replace(txt, "");
        return txt;
    }

    /**
     * 用枚举来区分是复制还是剪切
     */
    public enum SelectMode {
        COPY, CUT
    }
}
