package com.echoleaf.frame.views.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.echoleaf.frame.R;
import com.echoleaf.frame.utils.ViewUtils;
import com.echoleaf.frame.views.adapter.SingleTypeAdapter;

import java.util.List;

/**
 * Created by dydyt on 2016/7/11.
 */
public class MenuSheet extends SupportDialog {


    View cancelButton;
    View contentView;
    GridView menuButtons;
    View rootView;

    Animation mShowAction;
    MenuSheetAdapter adapter;
    float numColumns;
    boolean autoColumns;

    public MenuSheet(Context context) {
        this(context, null, 0);
    }

    public MenuSheet(Context context, List<MenuSheetObject> menus) {
        this(context, menus, 0);
    }

    public MenuSheet(Context context, List<MenuSheetObject> menus, int numColumns) {
        super(context, R.style.DefaultDialog);
        setContentView(R.layout.menu_sheet);
        rootView = findViewById(R.id.root_view);
        contentView = findViewById(R.id.content_view);
        cancelButton = findViewById(R.id.cancel_button);
        menuButtons = (GridView) findViewById(R.id.menu_buttons);
        if (numColumns <= 0) {
            autoColumns = true;
            numColumns = (menus != null && menus.size() < 4) ? menus.size() : 4;
        }
        menuButtons.setNumColumns(numColumns);
        this.numColumns = numColumns;

        adapter = new MenuSheetAdapter(context);
        menuButtons.setAdapter(adapter);
        if (menus != null)
            adapter.setItems(menus);

        View.OnClickListener dismiss = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        rootView.setOnClickListener(dismiss);
        cancelButton.setOnClickListener(dismiss);
        mShowAction = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0);
        mShowAction.setDuration(400);
        mShowAction.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.anim.accelerate_decelerate_interpolator));
    }


    @Override
    public void show() {
        super.show();
        contentView.startAnimation(mShowAction);
        contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        contentView.setVisibility(View.GONE);
        super.dismiss();
    }

    public void setMenus(List<MenuSheetObject> menus) {
        if (autoColumns) {
            numColumns = (menus != null && menus.size() < 4) ? menus.size() : 4;
            menuButtons.setNumColumns((int) numColumns);
        }
        adapter.setItems(menus);
    }

    public void setMenus(List<MenuSheetObject> menus, int numColumns) {
        this.numColumns = numColumns;
        autoColumns = false;
        menuButtons.setNumColumns(numColumns);
        adapter.setItems(menus);
    }


    class MenuSheetAdapter extends SingleTypeAdapter<MenuSheetObject> {
        Context context;

        public MenuSheetAdapter(Context context) {
            super(context, R.layout.menu_sheet_item);
            this.context = context;
        }

        @Override
        protected int[] getChildViewIds() {
            return new int[]{R.id.menu_button};
        }

        @Override
        protected void update(int position, MenuSheetObject item) {
            textView(0).setText(item.getTitle());
            if (item.getIconId() <= 0)
                textView(0).setCompoundDrawablesWithIntrinsicBounds(null, item.getIcon(), null, null);
            else
                textView(0).setCompoundDrawablesWithIntrinsicBounds(0, item.getIconId(), 0, 0);
            textView(0).setOnClickListener(item.getOnClickListener());
        }

        @Override
        public void setItems(Object[] items) {
            super.setItems(items);
            setGridViewHeightBasedOnChildren(menuButtons);
        }

        private void setGridViewHeightBasedOnChildren(GridView gridView) {
            ListAdapter listAdapter = gridView.getAdapter();
            if (listAdapter == null || listAdapter.getCount() == 0) {
                return;
            }
//        float numColumns = listView.getNumColumns();//建议设为固定值，否则UI第一次初始化的时候，通过GridView.getNumColumns();所取到的值是 -1.0

            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            int itemHeight = listItem.getMeasuredHeight();
            int item = (int) Math.floor(listAdapter.getCount() / numColumns);
            item = listAdapter.getCount() / numColumns == item ? item : item + 1;
            int totalHeight = (itemHeight * item);

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
//        int drviderHeight = ViewUtils.dip2px(this, 10) * item;

            int drviderHeight = ViewUtils.dip2px(context, 10.5f);
//            int drviderHeight = 0;
            params.height = totalHeight + drviderHeight;
            gridView.setLayoutParams(params);
        }
    }


}
