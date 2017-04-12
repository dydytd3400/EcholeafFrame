package com.echoleaf.frame.views.dialog;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by dydyt on 2016/9/8.
 */
public class MenuSheetObject {

    private View.OnClickListener onClickListener;
    private int iconId;
    private Drawable icon;
    private String title;

    public MenuSheetObject(String title, int iconId, View.OnClickListener onClickListener) {
        this.title = title;
        this.iconId = iconId;
        this.onClickListener = onClickListener;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
