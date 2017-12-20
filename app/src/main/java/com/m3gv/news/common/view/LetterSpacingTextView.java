package com.m3gv.news.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

import com.m3gv.news.R;


/**
 * Created by meikai on 17/4/11.
 */
public class LetterSpacingTextView extends AppCompatTextView {

    private float letterSpacing = Spacing.NORMAL;

    private CharSequence originalText = "";


    public LetterSpacingTextView(Context context) {
        super(context);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterSpacingTextView);
        originalText = array.getString(R.styleable.LetterSpacingTextView_letterSpacingText);
        letterSpacing = array.getDimension(R.styleable.LetterSpacingTextView_textSpacing, 0);
        array.recycle();

        setLetterSpacing(letterSpacing);
    }

    public float getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        applyLetterSpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    private void applyLetterSpacing() {
        if (this == null || this.originalText == null) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));

            // 在每个字符之间插入空格
            if (i + 1 < originalText.length()) {
                builder.append("\u00A0");  // "\u00A0" 表示 一个空格
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                // 将插入的空格 按 x 方向放大 (letterSpacing + 1) / 10 的倍数,  疑问：空格一定占10个像素？
                finalText.setSpan(new ScaleXSpan((letterSpacing + 1) / 10), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

    public class Spacing {
        public final static float NORMAL = 0;
        public final static float NORMALBIG = (float) 0.025;
        public final static float BIG = (float) 0.05;
        public final static float BIGGEST = (float) 0.2;
    }

}
