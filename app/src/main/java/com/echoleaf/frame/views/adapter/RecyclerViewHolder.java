package com.echoleaf.frame.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by echoleaf on 2017/4/21.
 */

public abstract class RecyclerViewHolder<E> extends RecyclerView.ViewHolder {
    protected View rootView;

    public RecyclerViewHolder(View rootView) {
        super(rootView);
        this.rootView = rootView;
    }

    protected abstract void renderView(E data, int position);

    protected View getRootView() {
        return rootView;
    }

    protected Context getContext() {
        return rootView.getContext();
    }
}