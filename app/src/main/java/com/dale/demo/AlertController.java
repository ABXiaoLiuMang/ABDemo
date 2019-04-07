//package com.dale.demo;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.view.LayoutInflater;
//import android.view.View;
//
///**
// * 文件描述:
// * 作者Dale:2019/3/15
// */
//public class AlertController {
//    public static class AlertParams {
//        public final Context mContext;
//        public final LayoutInflater mInflater;
//        public CharSequence mTitle;
//        public CharSequence mMessage;
//        public CharSequence mPositiveButtonText;
//        public CharSequence mNegativeButtonText;
//        public CharSequence mNeutralButtonText;
//        public Drawable mNeutralButtonIcon;
//        public View mView;
//        private int mViewLayoutResId;
//        public AlertParams(Context context) {
//            this.mContext = context;
//            this.mInflater = LayoutInflater.from(context);
//        }
//
//        public void apply(AlertController dialog) {
//            if (this.mTitle != null) {
//                dialog.setTitle(this.mTitle);
//            }
//
//            if (this.mMessage != null) {
//                dialog.setMessage(this.mMessage);
//            }
//
//            dialog.setView(this.mView);
//        }
//
//
//    }
//}
