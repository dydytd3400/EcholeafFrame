package com.echoleaf.frame.support;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.recyle.TrashCollector;
import com.echoleaf.frame.recyle.TrashMonitor;
import com.echoleaf.frame.support.controller.TouchEventController;
import com.echoleaf.frame.utils.StringUtils;
import com.echoleaf.frame.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何常平 on 2016/8/2.
 */
public class SupportActivity extends Activity implements SupportContext {
    protected Activity mContext;

    protected List<TouchEventController> controllers;
    protected List<Object> onFinishRecycle;
    protected List<Object> onDestoryRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void startActivity(Class<?> cls) {
        super.startActivity(new Intent(mContext, cls));
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        super.startActivityForResult(new Intent(mContext, cls), requestCode);
    }

    public void toastMessage(String msg) {
        if (StringUtils.notEmpty(msg))
            ViewUtils.toastMessage(mContext, msg);
    }

    public void toastMessage(int msg) {
        if (msg > 0)
            ViewUtils.toastMessage(mContext, msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (controllers != null)
            for (TouchEventController controller : controllers) {
                if (controller != null)
                    controller.processEvent(ev);
            }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        TrashCollector.monitor(this);
        if (onFinishRecycle != null) {
            for (Object o : onFinishRecycle) {
                if (o != null) {
                    if (o instanceof Trash) {
                        ((Trash) o).recycle();
                    }
                    o = null;
                }
            }
            onFinishRecycle = null;
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        TrashCollector.monitor(this);
        if (controllers != null) {
            controllers.clear();
            controllers = null;
        }
        if (onDestoryRecycle != null) {
            for (Object o : onDestoryRecycle) {
                if (o != null) {
                    if (o instanceof Trash) {
                        ((Trash) o).recycle();
                    }
                    o = null;
                }
            }
            onDestoryRecycle = null;
        }
        mContext = null;
        super.onDestroy();
    }

    @Override
    public void addTrash(Object trash, TrashMonitor.On on, int sort) {
        if (trash == null)
            return;
        List tarshes;
        if (on == TrashMonitor.On.FINISH) {
            if (this.onFinishRecycle == null)
                this.onFinishRecycle = new ArrayList<>();
            tarshes = onFinishRecycle;
        } else {
            if (this.onDestoryRecycle == null)
                this.onDestoryRecycle = new ArrayList<>();
            tarshes = onDestoryRecycle;
        }
        if (sort < 0)
            tarshes.add(trash);
        else
            tarshes.add(sort, trash);
    }

    public void addTouchEventController(TouchEventController... controllers) {
        if (controllers == null || controllers.length == 0)
            return;
        if (this.controllers == null)
            this.controllers = new ArrayList<>();
        for (TouchEventController controller : controllers) {
            this.controllers.add(controller);
            addTrash(controller, TrashMonitor.On.DESTORY, TrashMonitor.DISORDERED);
        }
    }
}