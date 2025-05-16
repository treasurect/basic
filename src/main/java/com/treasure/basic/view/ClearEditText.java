package com.treasure.basic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.treasure.basic.R;


public class ClearEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable clearIcon;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;
    private boolean isClearIconAlwaysVisible;
    private int clearIconPadding;

    public ClearEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        try {
            clearIcon = typedArray.getDrawable(R.styleable.ClearEditText_clearIcon);
            clearIconPadding = typedArray.getDimensionPixelSize(R.styleable.ClearEditText_clearIconPadding, 0);
        } finally {
            typedArray.recycle();
        }

        // 如果没有设置清除图标，使用默认图标
        if (clearIcon == null) {
            clearIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_edit_close, null);
        }

        // 设置图标边界
        setClearIconBounds();

        // 默认隐藏清除图标
        setClearIconVisible(false);

        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_r_8_f4f5f7_solid, null));
        setPadding(20,5,5,20);

        // 设置监听器
        super.setOnFocusChangeListener(this);
        super.setOnTouchListener(this);
        super.addTextChangedListener(this);
    }

    private void setClearIconBounds() {
        if (clearIcon != null) {
            clearIcon.setBounds(-clearIconPadding, -clearIconPadding, clearIcon.getIntrinsicWidth() + clearIconPadding, clearIcon.getIntrinsicHeight() + clearIconPadding);
        }
    }

    private void setClearIconVisible(boolean visible) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable endDrawable = visible && !getText().toString().isEmpty() ? clearIcon : null;

        setCompoundDrawables(compoundDrawables[0], // left
                compoundDrawables[1], // top
                endDrawable,          // right
                compoundDrawables[3]  // bottom
        );
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.onFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.onTouchListener = l;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && !isClearIconAlwaysVisible) {
            setClearIconVisible(!getText().toString().isEmpty());
        } else {
            setClearIconVisible(isClearIconAlwaysVisible && !getText().toString().isEmpty());
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedClearIcon = event.getX() > (getWidth() - getPaddingRight() - clearIcon.getIntrinsicWidth());
            if (tappedClearIcon) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                }
                return true;
            }
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // 不需要实现
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused() || isClearIconAlwaysVisible) {
            setClearIconVisible(!s.toString().isEmpty());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // 不需要实现
    }

    // 公开方法
    public void setClearIconAlwaysVisible(boolean visible) {
        this.isClearIconAlwaysVisible = visible;
        setClearIconVisible(visible && !getText().toString().isEmpty());
    }

    public void setClearIcon(Drawable drawable) {
        this.clearIcon = drawable;
        setClearIconBounds();
        setClearIconVisible(!getText().toString().isEmpty());
    }

    public void setClearIconPadding(int padding) {
        this.clearIconPadding = padding;
        setClearIconBounds();
        setClearIconVisible(!getText().toString().isEmpty());
    }
}
