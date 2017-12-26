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

import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView

import com.echoleaf.frame.utils.ViewUtils

import java.text.NumberFormat

/**
 * Updater for child views indexed from a root view
 */
class ViewUpdater {

    /**
     * Root view currently being updated
     */
    var view: View

    /**
     * Child views currently being updated
     */
    var childViews: Array<View>

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
    fun initialize(view: View, children: IntArray): View {
        val views = arrayOfNulls<View>(children.size)
        for (i in children.indices)
            views[i] = view.findViewById(children[i])
        view.tag = views
        this.view = view
        childViews = views
        return view
    }

    /**
     * Set current view that is currently being updated
     *
     * @param view
     */
    fun setCurrentView(view: View) {
        this.view = view
        childViews = getChildren(view)
    }

    /**
     * Get indexed children
     *
     * @param parentView
     * @return children
     */
    fun getChildren(parentView: View): Array<View> {
        return parentView.tag as Array<View>
    }

    /**
     * Get text view at given index
     *
     * @param childViewIndex
     * @return text view
     */
    fun textView(childViewIndex: Int): TextView {
        return childViews[childViewIndex] as TextView
    }

    /**
     * Get text view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return text view
     */
    fun textView(parentView: View, childViewIndex: Int): TextView {
        return getChildren(parentView)[childViewIndex] as TextView
    }

    /**
     * Get image view at given index
     *
     * @param childViewIndex
     * @return image view
     */
    fun imageView(childViewIndex: Int): ImageView {
        return childViews[childViewIndex] as ImageView
    }

    /**
     * Get image view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return image view
     */
    fun imageView(parentView: View, childViewIndex: Int): ImageView {
        return getChildren(parentView)[childViewIndex] as ImageView
    }

    /**
     * Get view at given index
     *
     * @param childViewIndex
     * @return view
     */
    fun <V : View> view(childViewIndex: Int): V {
        return childViews[childViewIndex] as V
    }

    /**
     * Get view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @return view
     */
    fun <V : View> view(parentView: View, childViewIndex: Int): V {
        return getChildren(parentView)[childViewIndex] as V
    }

    /**
     * Set text on text view at given index
     *
     * @param childViewIndex
     * @param text
     * @return text view
     */
    fun setText(childViewIndex: Int, text: CharSequence): TextView {
        val textView = textView(childViewIndex)
        textView.text = text
        return textView
    }

    /**
     * Set text on text view at given index
     *
     * @param parentView
     * @param childViewIndex
     * @param text
     * @return text view
     */
    fun setText(parentView: View, childViewIndex: Int, text: CharSequence): TextView {
        val textView = textView(parentView, childViewIndex)
        textView.text = text
        return textView
    }

    /**
     * Set text on text view at index to string resource
     *
     * @param childViewIndex
     * @param resourceId
     * @return text view
     */
    fun setText(childViewIndex: Int, resourceId: Int): TextView {
        val textView = textView(childViewIndex)
        textView.setText(resourceId)
        return textView
    }

    /**
     * Set text on text view at index to string resource
     *
     * @param parentView
     * @param childViewIndex
     * @param resourceId
     * @return text view
     */
    fun setText(parentView: View, childViewIndex: Int, resourceId: Int): TextView {
        val textView = textView(parentView, childViewIndex)
        textView.setText(resourceId)
        return textView
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
    fun setNumber(childViewIndex: Int, number: Long): TextView {
        val textView = textView(childViewIndex)
        textView.text = FORMAT_INT.format(number)
        return textView
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
    fun setNumber(parentView: View, childViewIndex: Int, number: Long): TextView {
        val textView = textView(parentView, childViewIndex)
        textView.text = FORMAT_INT.format(number)
        return textView
    }

    /**
     * Get child view
     *
     * @param childViewIndex
     * @param childViewClass
     * @return child view
     */
    fun <T> getView(childViewIndex: Int, childViewClass: Class<T>): T {
        return childViews[childViewIndex] as T
    }

    /**
     * Get child view
     *
     * @param parentView
     * @param childViewIndex
     * @param childViewClass
     * @return child view
     */
    fun <T> getView(parentView: View, childViewIndex: Int, childViewClass: Class<T>): T {
        return getChildren(parentView)[childViewIndex] as T
    }

    /**
     * Set child view as gone or visible
     *
     * @param childViewIndex
     * @param gone
     * @return child view
     */
    fun setGone(childViewIndex: Int, gone: Boolean): View? {
        return ViewUtils.setGone(view(childViewIndex), gone)
    }

    /**
     * Set child view as gone or visible
     *
     * @param parentView
     * @param childViewIndex
     * @param gone
     * @return child view
     */
    fun setGone(parentView: View, childViewIndex: Int, gone: Boolean): View? {
        return ViewUtils.setGone(view(parentView, childViewIndex), gone)
    }

    /**
     * Set the checked state of the [CompoundButton] with at index
     *
     * @param childViewIndex
     * @param checked
     * @return check box
     */
    fun setChecked(childViewIndex: Int, checked: Boolean): CompoundButton {
        val button = view<CompoundButton>(childViewIndex)
        button.isChecked = checked
        return button
    }

    /**
     * Set the checked state of the [CompoundButton] with at index
     *
     * @param parentView
     * @param childViewIndex
     * @param checked
     * @return check box
     */
    fun setChecked(parentView: View, childViewIndex: Int, checked: Boolean): CompoundButton {
        val button = view<CompoundButton>(parentView, childViewIndex)
        button.isChecked = checked
        return button
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
        val view = textView(childViewIndex)
        view.text = text
        ViewUtils.setGone(view, TextUtils.isEmpty(text))
        return view
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
        val view = textView(parentView, childViewIndex)
        view.text = text
        ViewUtils.setGone(view, TextUtils.isEmpty(text))
        return view
    }

    private fun formatRelativeTimeSpan(time: Long): CharSequence {
        return DateUtils.getRelativeTimeSpanString(time)
    }

    /**
     * Set relative time span on text view
     *
     * @param childViewIndex
     * @param time
     * @return text view
     */
    fun setRelativeTimeSpan(childViewIndex: Int, time: Long): TextView {
        return setText(childViewIndex, formatRelativeTimeSpan(time))
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
        return setText(parentView, childViewIndex, formatRelativeTimeSpan(time))
    }

    companion object {

        /**
         * Number formatter for integers
         */
        val FORMAT_INT = NumberFormat.getIntegerInstance()
    }
}
