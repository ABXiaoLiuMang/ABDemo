package com.lifecycle.plugin;

import android.app.Dialog;
import android.content.DialogInterface;

import com.lifecycle.Action;


/**
 * Desc:
 **/
public abstract class Plugin {
    protected Action mCreateAction;
    protected Action mStartAction;
    protected Action mResumeAction;
    protected Action mPauseAction;
    protected Action mStopAction;
    protected Action mDestroyAction;

    public Plugin() {
    }

    public abstract void apply();

    protected void setLifecycleListener(Dialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (Plugin.this.mDestroyAction != null) {
                            Plugin.this.mDestroyAction.accept();
                        }

                    }
                });
            }
        }
    }




    protected class PluginLifeCyclerListener implements LifecycleListener {
        protected PluginLifeCyclerListener() {
        }

        public void onCreate() {
            if (Plugin.this.mCreateAction != null) {
                Plugin.this.mCreateAction.accept();
            }

        }

        public void onStart() {
            if (Plugin.this.mStartAction != null) {
                Plugin.this.mStartAction.accept();
            }

        }

        public void onResume() {
            if (Plugin.this.mResumeAction != null) {
                Plugin.this.mResumeAction.accept();
            }

        }

        public void onPause() {
            if (Plugin.this.mPauseAction != null) {
                Plugin.this.mPauseAction.accept();
            }

        }

        public void onStop() {
            if (Plugin.this.mStopAction != null) {
                Plugin.this.mStopAction.accept();
            }

        }

        public void onDestroy() {
            if (Plugin.this.mDestroyAction != null) {
                Plugin.this.mDestroyAction.accept();
            }

        }
    }
}

