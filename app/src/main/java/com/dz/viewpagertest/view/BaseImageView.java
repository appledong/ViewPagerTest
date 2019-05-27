package com.dz.viewpagertest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.dz.viewpagertest.Utils.CompatUtils;

/**
 * Base ImageView
 *
 * @author winzows 2018/6/26
 */
@SuppressLint("AppCompatCustomView")
public abstract class BaseImageView extends ImageView {
    protected static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    protected static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    protected static final int COLORDRAWABLE_DIMENSION = 2;

    protected static final int DEFAULT_RADIUS_SIZE = 10;
    protected static final int DEFAULT_BORDER_WIDTH = 0;
    protected static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    protected static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT;
    protected static final boolean DEFAULT_BORDER_OVERLAY = false;

    protected RectF mDrawableRect = new RectF();
    protected RectF mBorderRect = new RectF();

    protected Matrix mShaderMatrix = new Matrix();
    protected Paint mBitmapPaint = new Paint();
    protected Paint mBorderPaint = new Paint();
    protected Paint mCircleBackgroundPaint = new Paint();

    protected int mBorderColor = DEFAULT_BORDER_COLOR;
    protected int mBorderWidth = DEFAULT_BORDER_WIDTH;
    protected int mCircleBackgroundColor = DEFAULT_CIRCLE_BACKGROUND_COLOR;

    protected Bitmap mBitmap;
    protected BitmapShader mBitmapShader;
    protected int mBitmapWidth;
    protected int mBitmapHeight;

    protected float mDrawableRadius = DEFAULT_RADIUS_SIZE;
    protected float mBorderRadius = DEFAULT_RADIUS_SIZE;

    protected ColorFilter mColorFilter;

    protected boolean mReady;
    protected boolean mSetupPending;
    protected boolean mBorderOverlay;
    protected boolean mDisableCircularTransformation;

    /**
     * 构造
     *
     * @param context context
     */
    public BaseImageView(Context context) {
        super(context);
        initData();
    }

    /**
     * 构造
     *
     * @param context context
     * @param attrs   attrs
     */
    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    /**
     * 构造
     *
     * @param context      context
     * @param attrs        attrs
     * @param defStyleAttr defStyleAttr
     */
    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mDrawableRect = new RectF();
        mBorderRect = new RectF();

        mShaderMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBorderPaint = new Paint();
        mCircleBackgroundPaint = new Paint();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    /**
     * Set or clear the paint's colorfilter, returning the parameter.
     */
    public void applyColorFilter() {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(mColorFilter);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        applyColorFilter();
        invalidate();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * setBorderColor
     *
     * @param borderColor borderColor
     */
    public void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }


    /**
     * setBorderColor
     *
     * @param borderColorRes borderColorRes
     * @deprecated Use {@link #setBorderColor(int)} instead
     */
    @Deprecated
    public void setBorderColorResource(@ColorRes int borderColorRes) {
        setBorderColor(CompatUtils.getColor(getContext(), borderColorRes));
    }

    public int getCircleBackgroundColor() {
        return mCircleBackgroundColor;
    }

    /**
     * 设置圆背景
     *
     * @param circleBackgroundColor circleBackgroundColor
     */
    public void setCircleBackgroundColor(@ColorInt int circleBackgroundColor) {
        if (circleBackgroundColor == mCircleBackgroundColor) {
            return;
        }

        mCircleBackgroundColor = circleBackgroundColor;
        mCircleBackgroundPaint.setColor(circleBackgroundColor);
        invalidate();
    }


    /**
     * updateShaderMatrix
     */
    protected void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        final float num = 0.5f;
        if (mShaderMatrix == null) {
            mShaderMatrix = new Matrix();
        }
        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * num;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * num;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + num) + mDrawableRect.left, (int) (dy + num) + mDrawableRect.top);
        if (mBitmapShader != null) {
            mBitmapShader.setLocalMatrix(mShaderMatrix);
        }
    }


    /**
     * OutlineProvider
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected class OutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            Rect bounds = new Rect();
            final float num = 2.0f;
            mBorderRect.roundOut(bounds);
            outline.setRoundRect(bounds, bounds.width() / num);
        }

    }

    /**
     * 获取bitmap
     *
     * @param drawable drawable
     * @return Bitmap
     */
    protected Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicHeight();
                if (width > 0 && height > 0) {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
                } else {
                    bitmap = null;
                }
            }
            if (bitmap != null) {
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 初始化Bitmap
     */
    protected void initializeBitmap() {
        if (mDisableCircularTransformation) {
            mBitmap = null;
        } else {
            mBitmap = getBitmapFromDrawable(getDrawable());
        }
        setup();
    }

    /**
     * 设置方法
     */
    protected abstract void setup();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    /**
     * superSetUp
     */
    protected void superSetUp() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mCircleBackgroundPaint.setStyle(Paint.Style.FILL);
        mCircleBackgroundPaint.setAntiAlias(true);
        mCircleBackgroundPaint.setColor(mCircleBackgroundColor);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
    }
}
