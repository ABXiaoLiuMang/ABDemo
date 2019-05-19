package com.cn.common.view;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.common.ui.R;
import com.cn.common.util.ABApplication;
import com.cn.common.util.ToastCompat;

//http://www.itboth.com/d/jiY73qyeAzei/api-java-toast-android

/**
 * @author div 2016-5-18 TODO
 */
public class ABToast {

	private static ToastCompat toast;
	private static TextView tv_toast;

	public static void show(String text) {
		if(toast == null){
			toast = new ToastCompat(ABApplication.getInstance());
			View view = LayoutInflater.from(ABApplication.getInstance()).inflate(R.layout.view_toast, null);
			tv_toast = view.findViewById(R.id.tv_toast);
			tv_toast.setTextColor(ABApplication.getInstance().getResources().getColor(android.R.color.white));
			toast.setGravity(Gravity.CENTER, 0,0);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(view);
		}
		tv_toast.setText(text);
		if(!TextUtils.isEmpty(text)){
			toast.show();
		}
	}

	public static void cancel() {
		if(toast != null){
			toast.cancel();
		}
	}
}
