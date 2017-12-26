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
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Cursor adapter for a single view type
 */
abstract class SingleTypeCursorAdapter
/**
 * Create adapter
 *
 * @param context
 * @param cursor
 * @param flags
 *
 * @param inflater
 * @param layoutResourceId
 */
(context: Context, cursor: Cursor, flags: Int, private val inflater: LayoutInflater, private val layout: Int) : CursorAdapter(context, cursor, flags) {

    private val children: IntArray

    /**
     * Current cursor being binded to
     */
    protected var cursor: Cursor

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
     * @param cursor
     * @param flags
     * @param layoutResourceId
     */
    constructor(activity: Activity, cursor: Cursor, flags: Int, layoutResourceId: Int) : this(activity, cursor, flags, activity.layoutInflater, layoutResourceId) {}

    /**
     * Create adapter
     *
     * @param context
     * @param cursor
     * @param flags
     * @param layoutResourceId
     */
    constructor(context: Context, cursor: Cursor, flags: Int, layoutResourceId: Int) : this(context, cursor, flags, LayoutInflater.from(context), layoutResourceId) {}

    init {

        var childIds: IntArray? = childViewIds
        if (childIds == null)
            childIds = IntArray(0)
        children = childIds
    }

    /**
     * Initialize view
     *
     * @param view
     * @return view
     */
    protected fun initialize(view: View): View {
        return updater.initialize(view, children)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        updater.setCurrentView(view)
        this.cursor = cursor
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return initialize(inflater.inflate(layout, null))
    }

    /**
     * Set text on child view to string of column index
     *
     * @param childViewIndex
     * @param columnIndex
     * @return text view
     */
    protected fun setText(childViewIndex: Int, columnIndex: Int): TextView {
        return updater.setText(childViewIndex, cursor.getString(columnIndex))
    }

    /**
     * Set text on child view to number at column index
     *
     * @param childViewIndex
     * @param columnIndex
     * @return text view
     */
    protected fun setNumber(childViewIndex: Int, columnIndex: Int): TextView {
        return updater.setNumber(childViewIndex, cursor.getLong(columnIndex))
    }

    /**
     * Set text on child view to time span from number at column index
     *
     * @param childViewIndex
     * @param columnIndex
     * @return text view
     */
    protected fun setRelativeTimeSpan(childViewIndex: Int, columnIndex: Int): TextView {
        return updater.setRelativeTimeSpan(childViewIndex, cursor.getLong(columnIndex))
    }
}
