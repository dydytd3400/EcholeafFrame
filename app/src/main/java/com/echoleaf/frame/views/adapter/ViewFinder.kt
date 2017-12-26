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
import android.content.res.Resources
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.ImageView
import android.widget.TextView

/**
 * Helper for finding and tweaking a view's children
 */
class ViewFinder {

    private val wrapper: FindWrapper

    private interface FindWrapper {

        val resources: Resources

        fun findViewById(id: Int): View
    }

    private class WindowWrapper internal constructor(private val window: Window) : FindWrapper {

        override val resources: Resources
            get() = window.context.resources

        override fun findViewById(id: Int): View {
            return window.findViewById(id)
        }
    }

    private class ViewWrapper internal constructor(private val view: View) : FindWrapper {

        override val resources: Resources
            get() = view.resources

        override fun findViewById(id: Int): View {
            return view.findViewById(id)
        }
    }

    /**
     * Create finder wrapping given view
     *
     * @param view
     */
    constructor(view: View) {
        wrapper = ViewWrapper(view)
    }

    /**
     * Create finder wrapping given window
     *
     * @param window
     */
    constructor(window: Window) {
        wrapper = WindowWrapper(window)
    }

    /**
     * Create finder wrapping given activity
     *
     * @param activity
     */
    constructor(activity: Activity) : this(activity.window) {}

    /**
     * Find view with id
     *
     * @param id
     * @return found view
     */
    fun <V : View> find(id: Int): V {
        return wrapper.findViewById(id) as V
    }

    /**
     * Get image view with id
     *
     * @param id
     * @return image view
     */
    fun imageView(id: Int): ImageView {
        return find(id)
    }

    /**
     * Get compound button with id
     *
     * @param id
     * @return image view
     */
    fun compoundButton(id: Int): CompoundButton {
        return find(id)
    }

    /**
     * Get text view with id
     *
     * @param id
     * @return text view
     */
    fun textView(id: Int): TextView {
        return find(id)
    }

    /**
     * Set text of child view with given id
     *
     * @param id
     * @param content
     * @return text view
     */
    fun setText(id: Int, content: CharSequence): TextView {
        val text = find<TextView>(id)
        text.text = content
        return text
    }

    /**
     * Set text of child view with given id
     *
     * @param id
     * @param content
     * @return text view
     */
    fun setText(id: Int, content: Int): TextView {
        return setText(id, wrapper.resources.getString(content))
    }

    /**
     * Register on click listener to child view with given id
     *
     * @param id
     * @param listener
     * @return view registered with listener
     */
    fun onClick(id: Int, listener: OnClickListener): View {
        val clickable = find<View>(id)
        clickable.setOnClickListener(listener)
        return clickable
    }

    /**
     * Register runnable to be invoked when child view with given id is clicked
     *
     * @param id
     * @param runnable
     * @return view registered with runnable
     */
    fun onClick(id: Int, runnable: Runnable): View {
        return onClick(id, OnClickListener { runnable.run() })
    }

    /**
     * Register on click listener with all given child view ids
     *
     * @param ids
     * @param listener
     */
    fun onClick(listener: OnClickListener, vararg ids: Int) {
        for (id in ids)
            find<View>(id).setOnClickListener(listener)
    }

    /**
     * Register runnable to be invoked when all given child view ids are clicked
     *
     * @param ids
     * @param runnable
     */
    fun onClick(runnable: Runnable, vararg ids: Int) {
        onClick(OnClickListener { runnable.run() }, *ids)
    }

    /**
     * Set drawable on child image view
     *
     * @param id
     * @param drawable
     * @return image view
     */
    fun setDrawable(id: Int, drawable: Int): ImageView {
        val image = imageView(id)
        image.setImageDrawable(image.resources.getDrawable(drawable))
        return image
    }

    /**
     * Register on checked change listener to child view with given id
     *
     * @param id
     * @param listener
     * @return view registered with listener
     */
    fun onCheck(id: Int,
                listener: OnCheckedChangeListener): CompoundButton {
        val checkable = find<CompoundButton>(id)
        checkable.setOnCheckedChangeListener(listener)
        return checkable
    }

    /**
     * Register runnable to be invoked when child view with given id is
     * checked/unchecked
     *
     * @param id
     * @param runnable
     * @return view registered with runnable
     */
    fun onCheck(id: Int, runnable: Runnable): CompoundButton {
        return onCheck(id, OnCheckedChangeListener { buttonView, isChecked -> runnable.run() })
    }

    /**
     * Register on checked change listener with all given child view ids
     *
     * @param ids
     * @param listener
     */
    fun onCheck(listener: OnCheckedChangeListener, vararg ids: Int) {
        for (id in ids)
            compoundButton(id).setOnCheckedChangeListener(listener)
    }

    /**
     * Register runnable to be invoked when all given child view ids are
     * checked/unchecked
     *
     * @param ids
     * @param runnable
     */
    fun onCheck(runnable: Runnable, vararg ids: Int) {
        onCheck(OnCheckedChangeListener { buttonView, isChecked -> runnable.run() }, *ids)
    }
}
