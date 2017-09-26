package com.example.commonlibrary.baseadapter.empty;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlibrary.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:39
 * QQ:             1981367757
 */

public class EmptyLayout extends FrameLayout implements View.OnClickListener {


    private FrameLayout errorLayout,emptyLayout;
    private LinearLayout loadingLayout;
    private FrameLayout container;
    private TextView loadContent;

    public EmptyLayout(@NonNull Context context) {
        this(context, null);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void initView(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.empty_layout);
        int bgColor = typedArray.getColor(R.styleable.empty_layout_empty_bg_color, Color.WHITE);
        View view = View.inflate(context, R.layout.empty_layout, this);
        errorLayout = (FrameLayout) view.findViewById(R.id.fl_empty_layout_error);
        emptyLayout= (FrameLayout) view.findViewById(R.id.fl_empty_layout_empty);

        loadContent = (TextView) view.findViewById(R.id.tv_empty_layout_loading_content);
        loadingLayout = (LinearLayout) view.findViewById(R.id.ll_empty_layout_loading);
        container = (FrameLayout) view.findViewById(R.id.fl_empty_layout_container);
        view.findViewById(R.id.tv_empty_error_retry).setOnClickListener(this);
        view.findViewById(R.id.tv_empty_empty_retry).setOnClickListener(this);
        container.setBackgroundColor(bgColor);
        typedArray.recycle();
        updateViewVisible();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public static final int STATUS_LOADING = 0;
    public static final int STATUS_NO_NET = 2;
    public static final int STATUS_NO_DATA = 3;
    public static final int STATUS_HIDE = 4;
    private int currentStatus = STATUS_HIDE;


    public int getCurrentStatus() {
        return currentStatus;
    }


    public void setLoadingContent(String message) {
        loadContent.setText(message);
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
        updateViewVisible();
    }

    private void updateViewVisible() {
        if (currentStatus != STATUS_HIDE) {
            if (getVisibility() == GONE) {
                setVisibility(VISIBLE);
            }
        } else {
            if (getVisibility() == VISIBLE) {
                setVisibility(GONE);
            }
        }
        switch (currentStatus) {
            case STATUS_HIDE:
                container.setVisibility(GONE);
                break;
            case STATUS_NO_DATA:
                container.setVisibility(VISIBLE);
                errorLayout.setVisibility(GONE);
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(VISIBLE);
            case STATUS_NO_NET:
                container.setVisibility(VISIBLE);
                errorLayout.setVisibility(VISIBLE);
                loadingLayout.setVisibility(GONE);
                emptyLayout.setVisibility(GONE);
                break;
            case STATUS_LOADING:
                container.setVisibility(VISIBLE);
                errorLayout.setVisibility(GONE);
                emptyLayout.setVisibility(GONE);
                loadingLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRetryListener != null) {
            mOnRetryListener.onRetry();
        }
    }


    public interface OnRetryListener {
        void onRetry();
    }


    private OnRetryListener mOnRetryListener;


    public void setOnRetryListener(OnRetryListener onRetryListener) {
        mOnRetryListener = onRetryListener;
    }


    //        定义一个注解，限定传入的状态值
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_HIDE, STATUS_LOADING, STATUS_NO_DATA, STATUS_NO_NET})
    public @interface EmptyStatus {

    }
}