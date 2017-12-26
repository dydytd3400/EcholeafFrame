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

import android.view.View
import android.widget.BaseAdapter
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView

import java.text.NumberFormat

/**
 * Base adapter
 */
abstract class TypeAdapter : BaseAdapter() {

    /**
     * Updater for current view
     */
    protected val updater = ViewUpdater()

    /**
     * Initialize view by binding indexed child views to tags on the root view
     *
     *
     * Sub-classes may override this method but must call super
     *
     * @param view
     * @param children
     * @return view
     */
    protected fun initialize(view: View, children: IntArray): View {
        return updater.initialize(view, children)
    }

    /**
     * Set current view that is currently being updated
     *
     * @param view
     */
    protected fun setCurrentView(view: View) {
        updater.setCurrentView(view)
    }

    /**
     * Get indexed children
     *
     * @param parentView
     * @return children
     */
    protected fun getChildren(parentView: View): Array<View> {
        return updater.getChildren(parentView)
    }

    /**
     * Get text view at given index
     *
     * @param childViewIndex
     * @return text view
     */
    protected fun textView(childViewIndex: Int): TextView {
        return updater.textView(childViewIndex)
    }

    /**
     * Get text view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return text view
     */
    protected fun textView(parentView: View, childViewIndex: Int): TextView {
        return updater.textView(parentView, childViewIndex)
    }

    /**
     * Get image view at given index
     *
     * @param childViewIndex
     * @return image view
     */
    protected fun imageView(childViewIndex: Int): ImageView {
        return updater.imageView(childViewIndex)
    }

    /**
     * Get image view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return image view
     */
    protected fun imageView(parentView: View, childViewIndex: Int): ImageView {
        return updater.imageView(parentView, childViewIndex)
    }

    /**
     * Get view at given index
     *
     * @param childViewIndex
     * @return view
     */
    protected fun <V : View> view(childViewIndex: Int): V {
        return updater.view(childViewIndex)
    }

    /**
     * Get view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return view
     */
    protected fun <V : View> view(parentView: View, childViewIndex: Int): V {
        return updater.view(parentView, childViewIndex)
    }

    /**
     * Set text on text view at given index
     *
     * @param childViewIndex
     * @param text
     * @return text view
     */
    protected fun setText(childViewIndex: Int, text: CharSequence): TextView {
        return updater.setText(childViewIndex, text)
    }

    /**
     * Set text on text view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @param text
     * @return text view
     */
    protected fun setText(parentView: View, childViewIndex: Int, text: CharSequence): TextView {
        return updater.setText(parentView, childViewIndex, text)
    }

    /**
     * Set text on text view at index to string resource
     *
     * @param childViewIndex
     * @param resourceId
     * @return text view
     */
    protected fun setText(childViewIndex: Int, resourceId: Int): TextView {
        return updater.setText(childViewIndex, resourceId)
    }

    /**
     * Set text on text view at index to string resource
     *
     * @param parentView
     * @param childViewIndex
     * @param resourceId
     * @return text view
     */
    protected fun setText(parentView: View, childViewIndex: Int, resourceId: Int): TextView {
        return updater.setText(parentView, childViewIndex, resourceId)
    }

    /**
     * Set text on text view to be formatted version of given integer number
     *
     *
     * This method uses the formatter returned from
     * [NumberFormat.getIntegerInstance]
     *
     * @param childViewIndex
     * @param number
     * @return text view
     */
    protected fun setNumber(childViewIndex: Int, number: Long): TextView {
        return updater.setNumber(childViewIndex, number)
    }

    /**
     * Set text on text view to be formatted version of given integer number
     *
     *
     * This method uses the formatter returned from
     * [NumberFormat.getIntegerInstance]
     *
     * @param parentView
     * @param childViewIndex
     * @param number
     * @return text view
     */
    protected fun setNumber(parentView: View, childViewIndex: Int, number: Long): TextView {
        return updater.setNumber(parentView, childViewIndex, number)
    }

    /**
     * Get child view
     *
     * @param childViewIndex
     * @param childViewClass
     * @return child view
     */
    protected fun <T> getView(childViewIndex: Int, childViewClass: Class<T>): T {
        return updater.getView(childViewIndex, childViewClass)
    }

    /**
     * Get child view
     *
     * @param parentView
     * @param childViewIndex
     * @param childViewClass
     * @return child view
     */
    protected fun <T> getView(parentView: View, childViewIndex: Int, childViewClass: Class<T>): T {
        return updater.getView(parentView, childViewIndex, childViewClass)
    }

    /**
     * Set child view as gone or visible
     *
     * @param childViewIndex
     * @param gone
     * @return child view
     */
    protected fun setGone(childViewIndex: Int, gone: Boolean): View? {
        return updater.setGone(childViewIndex, gone)
    }

    /**
     * Set child view as gone or visible
     *
     * @param parentView
     * @param childViewIndex
     * @param gone
     * @return child view
     */
    protected fun setGone(parentView: View, childViewIndex: Int, gone: Boolean): View? {
        return updater.setGone(parentView, childViewIndex, gone)
    }

    /**
     * Set the checked state of the [CompoundButton] with at index
     *
     * @param childViewIndex
     * @param checked
     * @return check box
     */
    protected fun setChecked(childViewIndex: Int, checked: Boolean): CompoundButton {
        return updater.setChecked(childViewIndex, checked)
    }

    /**
     * Set the checked state of the [CompoundButton] with at index
     *
     * @param parentView
     * @param childViewIndex
     * @param checked
     * @return check box
     */
    protected fun setChecked(parentView: View, childViewIndex: Int, checked: Boolean): CompoundButton {
        return updater.setChecked(parentView, childViewIndex, checked)
    }

    /**
     * Set the text on the text view if it is non-empty and make the view gone
     * if it is empty
     *
     * @param childViewIndex
     * @param text
     * @return text view
     */
    fun setVisibleText(childViewIndex: Int, text: CharSequence): TextView {
        return updater.setVisibleText(childViewIndex, text)
    }

    /**
     * Set the text on the text view if it is non-empty and make the view gone
     * if it is empty
     *
     * @param parentView
     *
     * @param childViewIndex
     * @param text
     * @return text view
     */
    fun setVisibleText(parentView: View, childViewIndex: Int, text: CharSequence): TextView {
        return updater.setVisibleText(parentView, childViewIndex, text)
    }

    /**
     * Set relative time span on text view
     *
     * @param childViewIndex
     * @param time
     * @return text view
     */
    fun setRelativeTimeSpan(childViewIndex: Int, time: Long): TextView {
        return updater.setRelativeTimeSpan(childViewIndex, time)
    }

    /**
     * Set relative time span on text view
     *
     * @param parentView
     * @param childViewIndex
     * @param time
     * @return text view
     */
    fun setRelativeTimeSpan(parentView: View, childViewIndex: Int, time: Long): TextView {
        return updater.setRelativeTimeSpan(parentView, childViewIndex, time)
    }
}
