/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.echoleaf.frame.views.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Adapter for lists where only a single view type is used
 *
 * @param <V>
</V> */
abstract class SingleTypeAdapter<V>
/**
 * Create adapter
 *
 * @param inflater
 * @param layoutResourceId
 */
(private val inflater: LayoutInflater, private val layout: Int) : TypeAdapter() {

    private val children: IntArray

    private var items: Array<Any>? = null

    /**
     * Get child view ids to store
     *
     *
     * The index of each id in the returned array should be used when using the
     * helpers to update a specific child view
     *
     * @return ids
     */
    protected abstract val childViewIds: IntArray

    /**
     * Create adapter
     *
     * @param activity
     * @param layoutResourceId
     */
    constructor(activity: Activity, layoutResourceId: Int) : this(activity.layoutInflater, layoutResourceId) {}

    /**
     * Create adapter
     *
     * @param context
     * @param layoutResourceId
     */
    constructor(context: Context, layoutResourceId: Int) : this(LayoutInflater.from(context), layoutResourceId) {}

    init {

        items = EMPTY

        var childIds: IntArray? = childViewIds
        if (childIds == null)
            childIds = IntArray(0)
        children = childIds
    }

    /**
     * Set items to display
     *
     * @param items
     */
    fun setItems(items: Collection<*>?) {
        if (items != null && !items.isEmpty())
            setItems(items.toTypedArray())
        else
            setItems(EMPTY)
    }

    /**
     * Set items to display
     *
     * @param items
     */
    open fun setItems(items: Array<Any>?) {
        if (items != null)
            this.items = items
        else
            this.items = EMPTY
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return items!!.size
    }

    override fun getItem(position: Int): V {
        return items!![position] as V
    }

    override fun getItemId(position: Int): Long {
        return items!![position].hashCode().toLong()
    }

    /**
     * Initialize view
     *
     * @param view
     * @return view
     */
    protected fun initialize(view: View): View {
        return super.initialize(view, children)
    }

    /**
     * Update view for item
     *
     * @param position
     * @param view
     * @param item
     */
    protected fun update(position: Int, view: View, item: V) {
        setCurrentView(view)
        update(position, item)
    }

    /**
     * Update item
     *
     * @param position
     * @param item
     */
    protected abstract fun update(position: Int, item: V)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null)
            convertView = initialize(inflater.inflate(layout, null))
        update(position, convertView, getItem(position))
        return convertView
    }

    companion object {

        private val EMPTY = arrayOfNulls<Any>(0)
    }
}
