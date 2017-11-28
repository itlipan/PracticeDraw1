package com.souche.fengche.appdemofort;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Lee on 2017/11/26.
 * 银行卡 EditText 4位加空格中断
 */

@SuppressLint("AppCompatCustomView")
public class BankEditText extends EditText {
    public interface AfterTextChangeListener{
        void afterTextChange(int infoLength);
    }

    private AfterTextChangeListener mChangeListener;

    public void setChangeListener(AfterTextChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public BankEditText(Context context) {
        super(context);
        initView(context);
    }

    public BankEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BankEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        this.addTextChangedListener(new AddSpaceTextWatcher(this, 25));
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
    }


    static class AddSpaceTextWatcher implements TextWatcher {

        private int beforeCharLength = 0;
        private int onTextLength = 0;

        private boolean isChanged = false;
        private boolean isSetText = false;

        private int spaceNumberBeforeChange = 0;

        private final int maxLength;
        private final BankEditText mEditText;

        private int cursorLocation;

        private StringBuffer mStringBufferCache = new StringBuffer();

        private AddSpaceTextWatcher(BankEditText etView, int maxLength) {
            this.maxLength = maxLength;
            this.mEditText = etView;
            etView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            clearStringBuffer();

            beforeCharLength = s.length();

            checkSpaceNumberBefore(s);
        }

        private void checkSpaceNumberBefore(CharSequence s) {
            spaceNumberBeforeChange = 0;
            if (!TextUtils.isEmpty(s)) {
                for (int i = 0; i < s.length(); i++) {
                    if (Character.isWhitespace(s.charAt(i))) {
                        spaceNumberBeforeChange++;
                    }
                }
            }
        }

        private void clearStringBuffer() {
            if (mStringBufferCache.length() > 0) {
                mStringBufferCache.delete(0, mStringBufferCache.length());
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextLength = s.length();//s => total edit info
            mStringBufferCache.append(s.toString());
            if (onTextLength == beforeCharLength
                || onTextLength > maxLength
                || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChanged) {// 内容改变会多次触发该监听,需要利用变量过滤,每次内容变更只执行一次
                mEditText.removeTextChangedListener(this);
                cursorLocation = mEditText.getSelectionEnd();

                int index = 0;
                while (index < mStringBufferCache.length()) { // remove all space
                    if (Character.isWhitespace(mStringBufferCache.charAt(index))){
                        mStringBufferCache.deleteCharAt(index);
                    }else {
                        index ++;
                    }
                }

                index = 0;
                int spaceNumberAfterInsert = 0;
                while (index < mStringBufferCache.length()){ // re start insert space
                    spaceNumberAfterInsert = insertSpace(index,spaceNumberAfterInsert);
                    index ++;
                }

                //calculate cursor index
                //最后一个数被输入,插入空格之后可能导致length 超过最大值  1111 1111 11111 插入空格=> 1111 1111 1111 1 最后的1 是多余的
                if (mStringBufferCache.length() > maxLength){
                    mStringBufferCache.delete(maxLength,mStringBufferCache.length());
                }
                index = 0;
                if (mStringBufferCache.length() > 0) {
                    while (Character.isWhitespace(mStringBufferCache.charAt(mStringBufferCache.length() - index - 1))){
                        index ++;
                    }
                }
                cursorLocation = mStringBufferCache.length() - index;
                mEditText.setText(mStringBufferCache.toString());
                try {
                    mEditText.setSelection(cursorLocation);
                }catch (Exception e){e.printStackTrace();}
                if (mEditText.mChangeListener != null){
                    mEditText.mChangeListener.afterTextChange(mStringBufferCache.length());
                }
                mEditText.addTextChangedListener(this);
                isChanged = false;
            }
        }

        private int insertSpace(int index ,int spaceNumberAfter){
            if (index > 3 && (index % (4 * (spaceNumberAfter + 1))) == spaceNumberAfter){
                mStringBufferCache.insert(index,' ');
                spaceNumberAfter ++;
            }
            return spaceNumberAfter;
        }

    }

}
