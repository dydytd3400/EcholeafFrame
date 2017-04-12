package com.echoleaf.frame.views.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.echoleaf.frame.R;
import com.echoleaf.frame.utils.ViewUtils;


public class LetterListView extends View {

    private OnTouchLetterChangeLister onTouchLetterChangeLister;

    private String py[] = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;
    private boolean showBk = false;
    private Paint paint = new Paint();

    private int textColor;
    private int selectColor;
    private int background;

    public LetterListView(Context context) {
        this(context, null);
    }

    public LetterListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttrs(context, attrs);
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.LetterListView);
        final int count = types.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = types.getIndex(i);
            if (attr == R.styleable.LetterListView_text_color) {
                textColor = types.getColor(attr, 0);
            } else if (attr == R.styleable.LetterListView_selected_color) {
                selectColor = types.getColor(attr, 0);
            } else if (attr == R.styleable.LetterListView_backcolor) {
                background = types.getColor(attr, 0);
            }
        }
        types.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final int oldChoose = choose;
        final float y = event.getY();
        final int c = (int) (y / getHeight() * py.length);
        final OnTouchLetterChangeLister lister = onTouchLetterChangeLister;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBk = true;
                if (oldChoose != c && lister != null) {
                    if (c >= 0 && c <= py.length) {
                        lister.onTouchLetterLister(py[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && lister != null) {
                    if (c >= 0 && c < py.length) {
                        lister.onTouchLetterLister(py[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBk = false;
                choose = -1;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBk) {
            canvas.drawColor(background);
        }
        int height = getHeight();
        int width = getWidth();
        int heightSingel = height / py.length;
        for (int i = 0; i < py.length; i++) {
            paint.setColor(textColor);
            paint.setTextSize(ViewUtils.dip2px(getContext(), 14));
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            if (i == choose) {
                paint.setColor(selectColor);
                paint.setFakeBoldText(true);
            }

            float xPos = width / 2 - paint.measureText(py[i]) / 2;
            float yPos = heightSingel * i + heightSingel;
            canvas.drawText(py[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchLetterChangeLister(OnTouchLetterChangeLister onTouchLetterChangeLister) {
        this.onTouchLetterChangeLister = onTouchLetterChangeLister;
    }

    public interface OnTouchLetterChangeLister {

        public void onTouchLetterLister(String s);
    }
}
