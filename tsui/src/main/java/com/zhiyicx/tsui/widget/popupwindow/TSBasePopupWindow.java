package com.zhiyicx.tsui.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.zhiyicx.tsui.utils.TSDeviceUtils;


/**
 * @Describe 自定义 popupindow,builder 模式
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public class TSBasePopupWindow extends PopupWindow {
    public static final float POPUPWINDOW_ALPHA = .8f;

    protected View mContentView;
    protected int mContentViewId;
    protected Activity mActivity;
    protected View mParentView;

    protected boolean isOutsideTouch;
    protected boolean isFocus;
    protected boolean isWrap;
    protected int mAnimationStyle;
    protected Drawable mBackgroundDrawable;
    protected CustomPopupWindowListener mListener;
    protected float mAlpha;
    protected int width;
    protected int height;

    protected TSBasePopupWindow(Builder builder) {
        this.mAlpha = builder.mAlpha;
        this.width = builder.width;
        this.height = builder.height;
        this.mActivity = builder.mActivity;
        this.mContentViewId = builder.contentViewId;
        this.mParentView = builder.parentView;
        this.mListener = builder.listener;
        this.isOutsideTouch = builder.isOutsideTouch;
        this.isFocus = builder.isFocus;
        this.mBackgroundDrawable = builder.backgroundDrawable;
        this.mAnimationStyle = builder.animationStyle;
        this.isWrap = builder.isWrap;
        initLayout();
    }

    protected void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(mContentViewId, null);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
        //mListener.initPopupView(mContentView);
        setWidth(isWrap ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT);
        setHeight(isWrap ? LayoutParams.WRAP_CONTENT : LayoutParams.MATCH_PARENT);
        if (width > 0) {
            setWidth(width);
        }
        if (height > 0) {
            setHeight(height);
        }
        if (mAlpha == 0){
            mAlpha = POPUPWINDOW_ALPHA;
        }
        setFocusable(isFocus);
        setOutsideTouchable(isOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        if (mAnimationStyle != -1)//如果设置了对话则使用对话
            setAnimationStyle(mAnimationStyle);
        setContentView(mContentView);
    }

    /**
     * 获得用于展示 popup 内容的 view
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 用于填充 contentView,必须传 ContextThemeWrapper (比如 activity )不然 popupwindow 要报错
     *
     * @param context
     * @param layoutId
     * @return
     */
    public static View inflateView(ContextThemeWrapper context, int layoutId) {
        return LayoutInflater.from(context)
                .inflate(layoutId, null);
    }

    /**
     * 默认显示到中间
     */
    public void show() {
        if (isShowing()) {
            dismiss();
            return;
        }
        setWindowAlpha(mAlpha);
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            showAtLocation(mParentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    public void showParentViewTop() {
        if (mParentView != null) {
            int[] location = new int[2];
            mParentView.getLocationOnScreen(location);
            showAtLocation(mParentView, Gravity.NO_GRAVITY,
                    mParentView.getWidth() - mContentView.getWidth(), location[1] - mParentView.getHeight() + TSDeviceUtils.getStatuBarHeight(mActivity));
        }
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void hide() {
        dismiss();
    }

    public static class Builder {
        protected float mAlpha;
        protected int contentViewId;
        protected View parentView;
        protected Activity mActivity;
        protected boolean isOutsideTouch = true;// 默认为true
        protected boolean isFocus = true;// 默认为true
        protected boolean isWrap;// 是否wrap_content
        protected int animationStyle = -1;
        protected Drawable backgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明
        protected CustomPopupWindowListener listener;

        private int width;
        private int height;

        protected Builder() {
        }

        public Builder backgroundAlpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder with(Activity activity) {
            this.mActivity = activity;
            return this;
        }

        public Builder contentView(int contentViewId) {
            this.contentViewId = contentViewId;
            return this;
        }

        public Builder parentView(View parentView) {
            this.parentView = parentView;
            return this;
        }

        public Builder isWrap(boolean isWrap) {
            this.isWrap = isWrap;
            return this;
        }


        public Builder customListener(CustomPopupWindowListener listener) {
            this.listener = listener;
            return this;
        }


        public Builder isOutsideTouch(boolean isOutsideTouch) {
            this.isOutsideTouch = isOutsideTouch;
            return this;
        }

        public Builder isFocus(boolean isFocus) {
            this.isFocus = isFocus;
            return this;
        }

        public Builder backgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder animationStyle(int animationStyle) {
            this.animationStyle = animationStyle;
            return this;
        }

        public TSBasePopupWindow build() {
            if (contentViewId <= 0)
                throw new IllegalStateException("contentView is required");
//            if (listener == null)
//                throw new IllegalStateException("CustomPopupWindowListener is required");

            return new TSBasePopupWindow(this);
        }
    }

    public interface CustomPopupWindowListener {
        void initPopupView(View contentView);
    }

}
