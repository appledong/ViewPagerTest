package com.dz.viewpagertest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;

import com.dz.viewpagertest.R;
import com.dz.viewpagertest.Utils.CompatUtils;

/**
 * RoundRectImageView
 *
 * @author Winzows on 2017/11/29.
 */

@SuppressLint("AppCompatCustomView")
public class RoundRectImageView extends BaseImageView {

    /**
     * 构造
     *
     * @param context context
     */
    public RoundRectImageView(Context context) {
        super(context);
        init();
    }

    /**
     * 构造
     *
     * @param context context
     * @param attrs   attrs
     */
    public RoundRectImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public RoundRectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.RoundRectImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.RoundRectImageView_border_color, DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.RoundRectImageView_border_overlay, DEFAULT_BORDER_OVERLAY);
        mDrawableRadius = a.getDimensionPixelSize(R.styleable.RoundRectImageView_radius_size, DEFAULT_RADIUS_SIZE);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundRectImageView_radius_size, DEFAULT_RADIUS_SIZE);

        if (a.hasValue(R.styleable.RoundRectImageView_circle_background_color)) {
            mCircleBackgroundColor = a.getColor(R.styleable.RoundRectImageView_circle_background_color, DEFAULT_CIRCLE_BACKGROUND_COLOR);
        } else if (a.hasValue(R.styleable.RoundRectImageView_fill_color)) {
            mCircleBackgroundColor = a.getColor(R.styleable.RoundRectImageView_fill_color, DEFAULT_CIRCLE_BACKGROUND_COLOR);
        }

        a.recycle();

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OutlineProvider());
        }

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }

        if (mBitmap == null) {
            return;
        }
        if (mCircleBackgroundColor != Color.TRANSPARENT) {
            canvas.drawRoundRect(mDrawableRect, mDrawableRadius, mDrawableRadius, mCircleBackgroundPaint);
        }
        canvas.drawRoundRect(mDrawableRect, mDrawableRadius, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth > 0) {
            canvas.drawRoundRect(mBorderRect, mBorderRadius, mBorderRadius, mBorderPaint);
        }
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    /**
     * setBorderOverlay
     *
     * @param borderOverlay borderOverlay
     */
    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }

        mBorderOverlay = borderOverlay;
        setup();
    }


    public int getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * 设置边缘宽度
     *
     * @param borderWidth borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    public boolean isDisableCircularTransformation() {
        return mDisableCircularTransformation;
    }

    private void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (mDisableCircularTransformation == disableCircularTransformation) {
            return;
        }

        mDisableCircularTransformation = disableCircularTransformation;
        initializeBitmap();
    }


    /**
     * 设置圆背景
     *
     * @param circleBackgroundRes circleBackgroundRes
     */
    public void setCircleBackgroundColorResource(@ColorRes int circleBackgroundRes) {
        int color = CompatUtils.getColor(getContext(), circleBackgroundRes);
        setCircleBackgroundColor(color);
    }

    /**
     * Return the color drawn behind the circle-shaped drawable.
     *
     * @return The color drawn behind the drawable
     * @deprecated Use {@link #getCircleBackgroundColor()} instead.
     */
    @Deprecated
    public int getFillColor() {
        return getCircleBackgroundColor();
    }


    /**
     * Set a color to be drawn behind the circle-shaped drawable. Note that
     * this has no effect if the drawable is opaque or no drawable is set.
     *
     * @param fillColor The color to be drawn behind the drawable
     * @deprecated Use {@link #setCircleBackgroundColor(int)} instead.
     */
    @Deprecated
    public void setFillColor(@ColorInt int fillColor) {
        setCircleBackgroundColor(fillColor);
    }

    /**
     * Set a color to be drawn behind the circle-shaped drawable. Note that
     * this has no effect if the drawable is opaque or no drawable is set.
     *
     * @param fillColorRes The color resource to be resolved to a color and
     *                     drawn behind the drawable
     * @deprecated Use {@link #setCircleBackgroundColorResource(int)} instead.
     */
    @Deprecated
    public void setFillColorResource(@ColorRes int fillColorRes) {
        setCircleBackgroundColorResource(fillColorRes);
    }


    public void setDrawableRadius(float radius) {
        mDrawableRadius = radius;
    }


    @Override
    public void setup() {
        if (mBorderRect == null || mDrawableRect == null) {
            return;
        }
        superSetUp();

        mBorderRect.set(calculateBounds());
        mDrawableRect.set(mBorderRect);

        applyColorFilter();
        updateShaderMatrix();
        invalidate();
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        return new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + availableWidth, getPaddingTop() + availableHeight);
    }

}
