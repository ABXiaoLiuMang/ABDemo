package com.lifecycle.plugin;

import android.app.Dialog;

import com.lifecycle.Action;


public class DialogPlugin extends Plugin {
    private Dialog mDialog;

    public DialogPlugin(Dialog dialog) {
        this.mDialog = dialog;
    }

    public void apply() {
        if (this.mDialog != null) {
            this.setLifecycleListener(this.mDialog);
        }
    }

    public DialogPlugin setDestroy(Action destroyAction) {
        this.mDestroyAction = destroyAction;
        return this;
    }
}
