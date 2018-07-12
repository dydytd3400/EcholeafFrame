package com.echoleaf.frame.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echoleaf.frame.recyle.Trash;
import com.echoleaf.frame.recyle.TrashCollector;
import com.echoleaf.frame.recyle.TrashMonitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by echoleaf on 2017/4/21.
 */

public abstract class RecyclerViewAdapter<E> extends RecyclerView.Adapter<RecyclerViewHolder<E>> implements Trash {
    private int layoutResId;
    private OnItemClickListener<E> onItemClickListener;
    private OnItemViewClickListener<E> onItemViewClickListener;
    @TrashMonitor
    protected ArrayList<E> dataList = new ArrayList<>();
    protected List<Integer> mOnItemClickWacthViewIds;

    public RecyclerViewAdapter(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public void setItems(Collection<E> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addItems(Collection<E> list) {
        int len = dataList.size();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
            notifyItemRangeInserted(len, list.size());
        }
    }

    public void removeItems(Collection<E> list) {
        int len = this.dataList.size();
        if (list != null && list.size() > 0) {
            this.dataList.removeAll(list);
            this.notifyItemRangeRemoved(len, list.size());
        }
    }

    public void addItem(E item) {
        dataList.add(item);
        notifyItemInserted(dataList.size() - 1);
    }

    public void addItem(int position, E item) {
        dataList.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(E item) {
        int i = indexOf(item);
        if (i < 0)
            return;
        if (dataList.remove(item))
            notifyItemRemoved(i);
    }


    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<E> datas() {
        return dataList;
    }

    @Override
    public RecyclerViewHolder<E> onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
    }

    protected abstract RecyclerViewHolder<E> createViewHolder(View view);

    @Override
    public void onBindViewHolder(RecyclerViewHolder<E> holder, final int position) {
        final E itemData = dataList.get(position);
        holder.renderView(itemData, position);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = indexOf(itemData);
                if (onItemViewClickListener != null && position >= 0)
                    onItemViewClickListener.onItemViewClick(v, itemData, position);
            }
        };
        View rootView = holder.getRootView();
        if (mOnItemClickWacthViewIds != null && mOnItemClickWacthViewIds.size() > 0) {
            for (int viewId : mOnItemClickWacthViewIds) {
                rootView.findViewById(viewId).setOnClickListener(onClickListener);
            }
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = indexOf(itemData);
                if (onItemClickListener != null && position >= 0)
                    onItemClickListener.onItemClick(v, itemData, position);
            }
        });
    }

    protected int indexOf(E o) {
        for (int i = 0; i < dataList.size(); i++)
            if (o == dataList.get(i))
                return i;
        return -1;
    }

    private void addOnItemClickWacthView(int id) {
        if (mOnItemClickWacthViewIds == null)
            mOnItemClickWacthViewIds = new ArrayList<>();
        mOnItemClickWacthViewIds.add(id);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener<E> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemViewClickListener(OnItemViewClickListener<E> onItemViewClickListener, int... viewIds) {
        this.onItemViewClickListener = onItemViewClickListener;
        if (viewIds == null || viewIds.length == 0)
            return;
        for (int id : viewIds) {
            addOnItemClickWacthView(id);
        }
    }

    public interface OnItemClickListener<E> {
        void onItemClick(View v, E item, int position);
    }

    public interface OnItemViewClickListener<E> {
        void onItemViewClick(View v, E item, int position);
    }

    @Override
    public void recycle() {
        onItemClickListener = null;
        TrashCollector.recycle(this);
    }


}