package com.echoleaf.frame.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by dydyt on 2016/7/1.
 */
public class ViewUtils {

    /**
     * @param textView
     * @param text
     * @return
     */
    public static float getTextWidth(TextView textView, String text) {
        return textView.getPaint().measureText(text);
    }

    public static float getTextWidth(TextView textView) {
        return textView.getPaint().measureText(textView.getText().toString());
    }


    /**
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 弹出Toast消息
     *
     * @param msg
     */
    public static void toastMessage(Context cont, String msg) {
        if (!StringUtils.isEmpty(msg))
            Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastMessage(Context cont, int msg) {
        String string = cont.getString(msg);
        toastMessage(cont, string);
    }

    public static void toastMessage(Context cont, String msg, int time) {
        if (!StringUtils.isEmpty(msg))
            Toast.makeText(cont, msg, time).show();
    }

    /**
     * Set visibility of given view to be gone or visible
     * <p>
     * This method has no effect if the view visibility is currently invisible
     *
     * @param view
     * @param gone
     * @return view
     */
    public static <V extends View> V setGone(final V view, final boolean gone) {
        if (view != null)
            if (gone) {
                if (GONE != view.getVisibility())
                    view.setVisibility(GONE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    /**
     * Set visibility of given view to be invisible or visible
     * <p>
     * This method has no effect if the view visibility is currently gone
     *
     * @param view
     * @param invisible
     * @return view
     */
    public static <V extends View> V setInvisible(final V view,
                                                  final boolean invisible) {
        if (view != null)
            if (invisible) {
                if (INVISIBLE != view.getVisibility())
                    view.setVisibility(INVISIBLE);
            } else {
                if (VISIBLE != view.getVisibility())
                    view.setVisibility(VISIBLE);
            }
        return view;
    }

    public static boolean isTouchPointInView(View view, float x, float y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();
        return y >= top && y <= bottom && x >= left && x <= right;
    }

    public static void copyToClipboard(Context context, String text) {
        copyToClipboard(context, text, null);
    }

    public static void copyToClipboard(Context context, String text, int successPoint) {
        copyToClipboard(context, text, successPoint == 0 ? null : context.getString(successPoint));
    }

    public static void copyToClipboard(Context context, String text, String successPoint) {
        if (StringUtils.notEmpty(text)) {
            if (android.os.Build.VERSION.SDK_INT > 11) {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
            } else {
                android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                c.setText(text);
            }
            if (StringUtils.notEmpty(successPoint)) {
                ViewUtils.toastMessage(context, successPoint);
            }
        }

    }

}
