package com.echoleaf.frame.views.dialog;

import android.app.Dialog;
import android.content.Context;

import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.recyle.TrashMonitor;
import com.echoleaf.frame.support.SupportContext;


/**
 * Created by dydyt on 2017/2/21.
 */

public class SupportDialog extends Dialog implements Trash {
    public SupportDialog(Context context) {
        super(context);
        if (context instanceof SupportContext) {
            ((SupportContext) context).addTrash(this, TrashMonitor.On.FINISH, TrashMonitor.DISORDERED);
        }
    }

    public SupportDialog(Context context, int themeResId) {
        super(context, themeResId);
        if (context instanceof SupportContext) {
            ((SupportContext) context).addTrash(this, TrashMonitor.On.FINISH, TrashMonitor.DISORDERED);
        }
    }

    protected SupportDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        if (context instanceof SupportContext) {
            ((SupportContext) context).addTrash(this, TrashMonitor.On.FINISH, TrashMonitor.DISORDERED);
        }
    }

    @Override
    public void recycle() {
        dismiss();
    }

}
