package com.echoleaf.frame.views.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echoleaf.frame.R;
import com.echoleaf.frame.utils.CollectionUtils;
import com.echoleaf.frame.utils.ViewUtils;


/**
 * Created by dydyt on 2016/7/11.
 */
public class ActionSheet<T> extends SupportDialog {

    public interface OnInitializeItemViewListener<T> {
        /**
         * 初始化ItemTextview时调用
         *
         * @param parentView
         * @param itemView
         * @param item
         * @param postion
         * @return 是否默认设置item点击关闭ActionSheet, 如果返回为true，则会覆盖调用该方法时为ItemView所设置的OnClick事件
         */
        boolean onInitialize(ActionSheet parentView, TextView itemView, T item, int postion);
    }

    Button cancelButton;
    LinearLayout itemContainer;
    private OnInitializeItemViewListener onInitializeItemViewListener;
    private T[] items;

    Context context;
    View.OnClickListener dismissClickListener;

    public ActionSheet(Context context) {
        this(context, null, (T[]) null);
    }

    public ActionSheet(Context context, T... items) {
        this(context, null, items);
    }

    public ActionSheet(Context context, OnInitializeItemViewListener<T> listener, T... items) {
        super(context, R.style.DefaultDialog);
        setContentView(R.layout.action_sheet);
        this.context = context;
        itemContainer = (LinearLayout) findViewById(R.id.item_container);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        dismissClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        cancelButton.setOnClickListener(dismissClickListener);

        setItems(listener, items);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setItems(OnInitializeItemViewListener listener, T... items) {
        setOnInitializeItemViewListener(listener);
        setItems(items);
    }

    public void setItems(T... items) {
        this.items = items;
        if (items != null && items.length > 0) {
            itemContainer.removeAllViews();
            for (int i = 0; i < items.length; i++) {
                addItem(i, items[i]);
            }
        }
    }

    public void addItem(T item) {
        addItem(items.length, item);
        items = CollectionUtils.addObjectToArray(items, item);
    }

    private void addItem(int postion, T item) {
        if (postion > 0) {
            View line = new View(context);
            line.setBackgroundColor(ContextCompat.getColor(context, R.color.echoleaf_light));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dip2px(context, 0.5f));
            line.setLayoutParams(layoutParams);
            itemContainer.addView(line);
        }
        TextView textView = new TextView(context);
        textView.setText(item == null ? "" : item.toString());
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(context, R.color.echoleaf_dark));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.action_sheet_height));
        textView.setLayoutParams(layoutParams);
        if (onInitializeItemViewListener != null) {
            boolean dismiss = onInitializeItemViewListener.onInitialize(this, textView, item, postion);
            if (dismiss) {
                textView.setOnClickListener(dismissClickListener);
            }
        }
        itemContainer.addView(textView);
    }

    public void setOnInitializeItemViewListener(OnInitializeItemViewListener onInitializeItemViewListener) {
        this.onInitializeItemViewListener = onInitializeItemViewListener;
    }

    public Button getCancleButton() {
        return cancelButton;
    }

    @Override
    public void show() {
        if (items != null && items.length > 0) {
            super.show();
        }
    }


}
