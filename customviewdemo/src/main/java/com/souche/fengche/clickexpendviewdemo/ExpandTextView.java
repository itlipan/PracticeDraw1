package com.souche.fengche.clickexpendviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by Lee on 2018/1/11.
 */

@SuppressLint("AppCompatCustomView")
public class ExpandTextView extends TextView {

    public static final int TYPE_EXPEND = 1;
    public static final int TYPE_SHRINK = 11;

    private int mCurrentTextState = TYPE_SHRINK;

    /**
     * 收起时最长展示 Text 行数
     */
    private int mMaxLineToShrink = 4;
    /**
     * 是否需要显示 收起 的 span
     */
    private boolean mIsNeedShowShrinkSpan = false;
    /**
     * 是否需要显示 展开 的 Span
     */
    private boolean mIsNeedShowExpandSpan = true;

    private String mHintInfoClickToExpand;
    private String mEllipsisEndOfText;
    private String mHintInfoClickToShrink;
    private String mGapBySpan;

    private CharSequence mOriginFullText;
    private BufferType mBufferType;

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context, attrs);
    }

    private static <T> T getAttrsWithDefault(T attrValue, T defaultValue) {
        if (attrValue instanceof CharSequence) {
            if (TextUtils.isEmpty((CharSequence) attrValue)) {
                return attrValue;
            }
        } else if (attrValue instanceof Integer) {
            if ((Integer) attrValue <= 0) {
                return attrValue;
            }
        }
        return defaultValue;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray tr = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        if (tr == null) return;

        mMaxLineToShrink = getAttrsWithDefault(tr.getIndex(R.styleable.ExpandTextView_shrinkShowMaxLine),4);
        mIsNeedShowExpandSpan = tr.getBoolean(R.styleable.ExpandTextView_needShowExpandSpan, true);
        mIsNeedShowShrinkSpan = tr.getBoolean(R.styleable.ExpandTextView_needShowShrinkSpan, false);

        mEllipsisEndOfText = getAttrsWithDefault(tr.getString(R.styleable.ExpandTextView_ellipsisBeforeSpan),"...");
        mGapBySpan = getAttrsWithDefault(tr.getString(R.styleable.ExpandTextView_gapBeforeSpan),"  ");
        mHintInfoClickToShrink = getAttrsWithDefault(tr.getString(R.styleable.ExpandTextView_shrinkSpanHintInfo),"收起");
        mHintInfoClickToExpand = getAttrsWithDefault(tr.getString(R.styleable.ExpandTextView_expandSpanHintInfo),"展开");

        tr.recycle();
    }

    private TouchableSpan mTouchableSpan;

    private void initView(Context context, AttributeSet attrs) {
        mTouchableSpan = new TouchableSpan();

        setMovementMethod(new LinkTouchMovementMethod());

        afterSetTextChangeViewObserver();
    }

    private void afterSetTextChangeViewObserver() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ViewTreeObserver obs = getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
                setTextInternal(makeNewTextByCurrentViewState(), mBufferType);
            }
        });
    }


    /**
     * 流程:  setText 触发 setText(CharSequence text, BufferType type)
     * -- 进而完成 text 文本信息的获取与 BufferType 的初始化获取
     * -- 在设置文本后触发 View 的 Layout 进而 OnGlobalLayoutListener监听到对应 layout 信息
     * -- 最终读取其 layout 信息,动态设置改变其文本信息
     *
     * @param text
     * @param type
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        mBufferType = type;
        mOriginFullText = text;
        setTextInternal(text, type);
    }

    private void setTextInternal(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    private int getStringLength(CharSequence info) {
        return TextUtils.isEmpty(info) ? 0 : info.length();
    }

    private String getStringContent(CharSequence info) {
        return TextUtils.isEmpty(info) ? "" : info.toString();
    }

    /**
     * 根据当前显示状态展开收起生成对应的多行 Info
     *
     * @return CharSequence / SpannableString
     */
    public CharSequence makeNewTextByCurrentViewState() {

        if (getStringLength(mOriginFullText) <= 1) {
            return mOriginFullText;
        }

        final int contentLength = mOriginFullText.length();

        Layout txtLayout = getLayout();
        int txtWidth = txtLayout == null ? 0 : txtLayout.getWidth();
        if (txtWidth <= 0) {
            txtWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        }

        txtLayout = getDynamicLayout(txtWidth);
        // 小于需要显示展开收起行,直接显示原文本 || 展开同理
        final int originLineCount = txtLayout.getLineCount();
        if (originLineCount < mMaxLineToShrink) return mOriginFullText;

        if (mCurrentTextState == TYPE_SHRINK) {
            // 获取最后一行,显示 Clickable Span 行的 原始数据 index
            final int lastLineIndexEnd = txtLayout.getLineEnd(mMaxLineToShrink - 1);// line 从 0 开始
            final int lastLineIndexStart = txtLayout.getLineStart(mMaxLineToShrink - 1);

            final TextPaint txtPaint = getPaint();
            final int txtLayoutWidth = txtLayout.getWidth();
            int tailReplacedWidth = (int) txtPaint.measureText(getStringContent(mEllipsisEndOfText + (mIsNeedShowExpandSpan ? (mGapBySpan + mHintInfoClickToExpand) : "")));
            if (tailReplacedWidth > txtLayoutWidth) {
                mIsNeedShowExpandSpan = false;
                tailReplacedWidth = (int) txtPaint.measureText(getStringContent(mEllipsisEndOfText));
            }

            final int endLineTxtWidth = (int) (txtPaint.measureText(getStringContent(mOriginFullText)
                .substring(lastLineIndexStart, lastLineIndexEnd)) + 0.5);
            int offsetIndex = 0;
            int offsetWidth = endLineTxtWidth;
            if (tailReplacedWidth + offsetWidth > txtLayoutWidth) {
                while (tailReplacedWidth + offsetWidth > txtLayoutWidth) {
                    offsetIndex++;
                    offsetWidth = (int) (txtPaint.measureText(getStringContent(mOriginFullText)
                        .substring(lastLineIndexStart, lastLineIndexEnd - offsetIndex)) + 0.5);
                }
            }
            final int finalTrimIndex = lastLineIndexEnd - offsetIndex;
            String finalInfo = removeEndLineBreak(mOriginFullText.subSequence(0, finalTrimIndex));
            SpannableStringBuilder shrinkSpan = new SpannableStringBuilder(finalInfo)
                .append(mEllipsisEndOfText + (mIsNeedShowExpandSpan ? (mGapBySpan + mHintInfoClickToExpand) : ""));
            if (mIsNeedShowExpandSpan) {
                final int totalSpanLength = shrinkSpan.length();
                shrinkSpan.setSpan(mTouchableSpan, totalSpanLength - getStringLength(mHintInfoClickToExpand), totalSpanLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return shrinkSpan;
        } else if (mCurrentTextState == TYPE_EXPEND) {
            if (!mIsNeedShowShrinkSpan) return mOriginFullText;

            final SpannableStringBuilder expandSpan = new SpannableStringBuilder(mOriginFullText + mGapBySpan + mHintInfoClickToShrink);
            final int totalLength = expandSpan.length();
            expandSpan.setSpan(mTouchableSpan, totalLength - mHintInfoClickToShrink.length(), totalLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return expandSpan;
        }
        return mOriginFullText;
    }

    private String removeEndLineBreak(CharSequence text) {
        String str = text.toString();
        while (str.endsWith("\n")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void expandView() {
        mCurrentTextState = TYPE_EXPEND;
        setTextInternal(makeNewTextByCurrentViewState(), mBufferType);
    }

    private void shrinkView() {
        mCurrentTextState = TYPE_SHRINK;
        setTextInternal(makeNewTextByCurrentViewState(), mBufferType);
    }

    private Layout mCacheDynamicLayout;

    private Layout getDynamicLayout(int txtWidth) {
        if (mCacheDynamicLayout == null) {
            mCacheDynamicLayout = new DynamicLayout(mOriginFullText, getPaint(), txtWidth,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        }
        return mCacheDynamicLayout;
    }


    private class TouchableSpan extends ClickableSpan {
        private static final int TO_EXPAND_HINT_COLOR = 0xFF3498DB;
        private static final int TO_SHRINK_HINT_COLOR = 0xFFE74C3C;
        private static final int TO_EXPAND_HINT_COLOR_BG_PRESSED = 0x55999999;
        private static final int TO_SHRINK_HINT_COLOR_BG_PRESSED = 0x55999999;

        private boolean mIsPressed;

        private void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View widget) {
            switch (mCurrentTextState){
                case TYPE_SHRINK:
                    expandView();
                    break;
                case TYPE_EXPEND:
                    shrinkView();
                    break;
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            switch (mCurrentTextState){
                case TYPE_SHRINK:
                    ds.setColor(TO_SHRINK_HINT_COLOR);
                    ds.bgColor = mIsPressed ? TO_SHRINK_HINT_COLOR_BG_PRESSED : 0;
                    break;
                case TYPE_EXPEND:
                    ds.setColor(TO_EXPAND_HINT_COLOR);
                    ds.bgColor = mIsPressed ? TO_EXPAND_HINT_COLOR_BG_PRESSED : 0;
                    break;
            }
            ds.setUnderlineText(false);
        }
    }

    /**
     * Copy from:
     * http://stackoverflow.com/questions
     * /20856105/change-the-text-color-of-a-single-clickablespan-when-pressed-without-affecting-o
     * By:
     * Steven Meliopoulos
     */
    private static class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                        spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
    }
}
