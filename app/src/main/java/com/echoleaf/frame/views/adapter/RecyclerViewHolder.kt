package com.echoleaf.frame.views.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by echoleaf on 2017/4/21.
 */

abstract class RecyclerViewHolder<E>(rootView: View) : RecyclerView.ViewHolder(rootView) {
    var rootView: View
        protected set

    protected val context: Context
        get() = rootView.context

    init {
        this.rootView = rootView
    }

    abstract fun renderView(data: E, position: Int)
}