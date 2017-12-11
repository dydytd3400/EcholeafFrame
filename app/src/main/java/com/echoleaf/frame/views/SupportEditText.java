package com.echoleaf.frame.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.echoleaf.frame.R;
import com.echoleaf.frame.utils.StringUtils;

/**
 * Created by dydyt on 2017/3/22.
 */

public class SupportEditText extends EditText {
    private boolean emojiAble;

    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    public SupportEditText(Context context) {
        super(context);
        readAttrs(context, null);
        init();
    }

    public SupportEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(context, attrs);
        init();
    }

    public SupportEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SupportEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttrs(context, attrs);
        init();
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.SupportEditText);
        final int count = types.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = types.getIndex(i);
            if (attr == R.styleable.SupportEditText_emoji_able) {
                emojiAble = types.getBoolean(attr, false);
            }
        }
        types.recycle();
    }

    public void setEmojiAble(boolean emojiAble) {
        this.emojiAble = emojiAble;
    }

    private void init() {
        initEditText();
    }

    private void initEditText() {
        cursorPos = getSelectionEnd();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!resetText && !emojiAble) {
                    cursorPos = getSelectionEnd();
                    //这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!emojiAble)
                    if (!resetText) {
                        if (before != 0) {
                            return;
                        }
                        if (count >= 2) {//表情符号的字符长度最小为2
                            CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                            boolean hasEmoji = false;
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < input.length(); i++) {
                                char c = input.charAt(i);
                                if (!StringUtils.isEmoji(c)) {
                                    sb.append(c);
                                } else {
                                    hasEmoji = true;
                                }
                            }
                            if (hasEmoji) {
                                resetText = true;
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                inputAfterText += sb.toString();
                                setText(inputAfterText);
                                CharSequence text = getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            }
                        }
                    } else {
                        resetText = false;
                    }
            }


//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!resetText && !emojiAble) {
//                    if (before != 0) {
//                        return;
//                    }
//                    if (count >= 2) {//表情符号的字符长度最小为2
//                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
//                        if (StringUtils.containsEmoji(input.toString())) {
//                            resetText = true;
//                            //是表情符号就将文本还原为输入表情符号之前的内容
//                            setText(inputAfterText);
//                            CharSequence text = getText();
//                            if (text instanceof Spannable) {
//                                Spannable spanText = (Spannable) text;
//                                Selection.setSelection(spanText, text.length());
//                            }
//                        }
//                    }
//                } else {
//                    resetText = false;
//                }
//            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
