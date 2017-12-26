package com.echoleaf.frame.views.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.echoleaf.frame.recyle.Trash
import com.echoleaf.frame.recyle.TrashCollector
import com.echoleaf.frame.recyle.TrashMonitor
import java.util.*

/**
 * Created by echoleaf on 2017/4/21.
 */

abstract class RecyclerViewAdapter<E>(private val layoutResId: Int) : RecyclerView.Adapter<RecyclerViewHolder<E>>(), Trash {
    private var onItemClickListener: OnItemClickListener<E>? = null
    private var onItemViewClickListener: OnItemViewClickListener<E>? = null
    @TrashMonitor
    private val dataList = ArrayList<E>()
    protected var mOnItemClickWacthViewIds: MutableList<Int>? = null

    fun setItems(list: Collection<E>?) {
        dataList.clear()
        if (list != null && list.size > 0) {
            dataList.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addItems(list: Collection<E>?) {
        val len = dataList.size
        if (list != null && list.size > 0) {
            dataList.addAll(list)
            notifyItemRangeInserted(len, list.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<E> {
        return createViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    }

    protected abstract fun createViewHolder(view: View): RecyclerViewHolder<E>

    override fun onBindViewHolder(holder: RecyclerViewHolder<E>, position: Int) {
        holder.renderView(dataList[position], position)
        val onClickListener = View.OnClickListener { v ->
            if (onItemViewClickListener != null)
                onItemViewClickListener!!.onItemViewClick(v, dataList[position], position)
        }
        val rootView = holder.rootView
        if (mOnItemClickWacthViewIds != null && mOnItemClickWacthViewIds!!.size > 0) {
            for (viewId in mOnItemClickWacthViewIds!!) {
                rootView.findViewById(viewId).setOnClickListener(onClickListener)
            }
        }

        rootView.setOnClickListener { v ->
            if (onItemClickListener != null)
                onItemClickListener!!.onItemClick(v, dataList[position], position)
        }
    }

    private fun addOnItemClickWacthView(id: Int) {
        if (mOnItemClickWacthViewIds == null)
            mOnItemClickWacthViewIds = ArrayList()
        mOnItemClickWacthViewIds!!.add(id)
    }

    override fun getItemCount(): Int {
        return dataList?.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<E>) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener<E>, vararg viewIds: Int) {
        this.onItemViewClickListener = onItemViewClickListener
        if (viewIds == null || viewIds.size == 0)
            return
        for (id in viewIds) {
            addOnItemClickWacthView(id)
        }
    }

    interface OnItemClickListener<E> {
        fun onItemClick(v: View, item: E, position: Int)
    }

    interface OnItemViewClickListener<E> {
        fun onItemViewClick(v: View, item: E, position: Int)
    }

    override fun recycle() {
        onItemClickListener = null
        TrashCollector.recycle(this)
    }
}