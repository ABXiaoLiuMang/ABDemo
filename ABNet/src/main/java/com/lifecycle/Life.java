package com.lifecycle;

import android.app.Dialog;

import com.lifecycle.plugin.DialogPlugin;

public class Life {
    private Life() {
    }

    public static DialogPlugin beginTransaction(Dialog dialog) {
        return new DialogPlugin(dialog);
    }
}
