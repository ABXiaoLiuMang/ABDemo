//package com.dale.demo;
//
//import android.app.Activity;
//import android.arch.lifecycle.LifecycleOwner;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.AttrRes;
//import android.support.annotation.CallSuper;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.StringRes;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.RelativeLayout;
//
//import org.jetbrains.annotations.NotNull;
//
///**
// * 文件描述:
// * 作者Dale:2019/3/15
// */
//public class BaseDialog implements BaseLifecycleInterface {
//
//
//    private Context context;
//    RelativeLayout dialogRootView;
//    FrameLayout rootView;
//    AlertController mAlert;
//
//    public BaseDialog(Context context) {
//        this.context = context;
//        if(!(context instanceof Activity)){
//            new Throwable("context 必须是Activity的子类");
//        }
//        rootView = ((Activity)context).findViewById(android.R.id.content);
//        dialogRootView = new RelativeLayout(context);
//        dialogRootView.setId(R.id.contentView);
//    }
//
//
//    @CallSuper
//    @Override
//    public void onStop(@NotNull LifecycleOwner owner) {
//         if(rootView.findViewById(R.id.contentView) != null){
//             rootView.removeView(dialogRootView);
//         }
//    }
//
//    public BaseDialog setView(View view){
//        dialogRootView.addView(view);
//        return this;
//    }
//
//    public void show(){
//        if(rootView.findViewById(R.id.contentView) == null){
//            rootView.addView(dialogRootView);
//        }
//    }
//
//    public void dismiss(){
//        if(rootView.findViewById(R.id.contentView) != null){
//            rootView.removeView(dialogRootView);
//        }
//    }
//
//    public static class Builder {
//        private final AlertController.AlertParams P;
//
//        public Builder(@NonNull Context context) {
//            this.P = new AlertController.AlertParams(context);
//        }
//
//        @NonNull
//        public Context getContext() {
//            return this.P.mContext;
//        }
//
//        public BaseDialog.Builder setTitle(@StringRes int titleId) {
//            this.P.mTitle = this.P.mContext.getText(titleId);
//            return this;
//        }
//
//        public BaseDialog.Builder setTitle(@Nullable CharSequence title) {
//            this.P.mTitle = title;
//            return this;
//        }
//
//        public BaseDialog.Builder setCustomTitle(@Nullable View customTitleView) {
//            this.P.mCustomTitleView = customTitleView;
//            return this;
//        }
//
//        public BaseDialog.Builder setMessage(@StringRes int messageId) {
//            this.P.mMessage = this.P.mContext.getText(messageId);
//            return this;
//        }
//
//        public BaseDialog.Builder setMessage(@Nullable CharSequence message) {
//            this.P.mMessage = message;
//            return this;
//        }
//
//
//        public BaseDialog.Builder setIconAttribute(@AttrRes int attrId) {
//            TypedValue out = new TypedValue();
//            this.P.mContext.getTheme().resolveAttribute(attrId, out, true);
//            return this;
//        }
//
//        public BaseDialog.Builder setPositiveButton(@StringRes int textId, View.OnClickListener listener) {
//            this.P.mPositiveButtonText = this.P.mContext.getText(textId);
//            this.P.mPositiveButtonListener = listener;
//            return this;
//        }
//
//        public BaseDialog.Builder setPositiveButton(CharSequence text, View.OnClickListener listener) {
//            this.P.mPositiveButtonText = text;
//            this.P.mPositiveButtonListener = listener;
//            return this;
//        }
//
//
//
//        public BaseDialog.Builder setNegativeButton(@StringRes int textId, View.OnClickListener listener) {
//            this.P.mNegativeButtonText = this.P.mContext.getText(textId);
//            this.P.mNegativeButtonListener = listener;
//            return this;
//        }
//
//        public BaseDialog.Builder setNegativeButton(CharSequence text, View.OnClickListener listener) {
//            this.P.mNegativeButtonText = text;
//            this.P.mNegativeButtonListener = listener;
//            return this;
//        }
//
//        public BaseDialog.Builder setNegativeButtonIcon(Drawable icon) {
//            this.P.mNegativeButtonIcon = icon;
//            return this;
//        }
//
//        public BaseDialog.Builder setNeutralButton(@StringRes int textId, View.OnClickListener listener) {
//            this.P.mNeutralButtonText = this.P.mContext.getText(textId);
//            this.P.mNeutralButtonListener = listener;
//            return this;
//        }
//
//        public BaseDialog.Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
//            this.P.mNeutralButtonText = text;
//            this.P.mNeutralButtonListener = listener;
//            return this;
//        }
//
//        public BaseDialog.Builder setNeutralButtonIcon(Drawable icon) {
//            this.P.mNeutralButtonIcon = icon;
//            return this;
//        }
//
//        public BaseDialog.Builder setCancelable(boolean cancelable) {
//            this.P.mCancelable = cancelable;
//            return this;
//        }
//
//        public BaseDialog.Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
//            this.P.mOnCancelListener = onCancelListener;
//            return this;
//        }
//
//        public BaseDialog.Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
//            this.P.mOnDismissListener = onDismissListener;
//            return this;
//        }
//
//
//
//
//        public BaseDialog.Builder setView(int layoutResId) {
//            this.P.mView = null;
//            this.P.mViewLayoutResId = layoutResId;
//            return this;
//        }
//
//        public BaseDialog.Builder setView(View view) {
//            this.P.mView = view;
//            this.P.mViewLayoutResId = 0;
//            return this;
//        }
//
//
//
//
//        public BaseDialog create() {
//            BaseDialog dialog = new BaseDialog(this.P.mContext);
//            this.P.apply(dialog.mAlert);
//            dialog.setCancelable(this.P.mCancelable);
//            if (this.P.mCancelable) {
//                dialog.setCanceledOnTouchOutside(true);
//            }
//
//            dialog.setOnCancelListener(this.P.mOnCancelListener);
//            dialog.setOnDismissListener(this.P.mOnDismissListener);
//            if (this.P.mOnKeyListener != null) {
//                dialog.setOnKeyListener(this.P.mOnKeyListener);
//            }
//
//            return dialog;
//        }
//
//        public BaseDialog show() {
//            BaseDialog dialog = this.create();
//            dialog.show();
//            return dialog;
//        }
//    }
//
//}
