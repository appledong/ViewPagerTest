package com.dz.viewpagertest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.dz.viewpagertest.R;
import com.dz.viewpagertest.Utils.DimensionPixelUtil;

public class AdapterImageView extends RoundRectImageView {

    /**
     * 图片模式：0：不做处理 1：单行列表模式 2：三个一行列表模式 3：width：match height：exactly
     */
    public final static int IMGAEVIEW_MODE_COMMON = 0;
    public final static int IMGAEVIEW_MODE_SINGLE = 1;
    public final static int IMGAEVIEW_MODE_MORE = 2;
    public final static int IMGAEVIEW_MODE_MATCH = 3;
    public final static int IMGAEVIEW_MODE_EXACTLY = 4;

    private Context mContext;

    private int imageMode = IMGAEVIEW_MODE_COMMON;
    /**
     * 默认宽高是单个view默认宽高
     */
    private int imageWidth = 0;
    private int imageHeight = 0;
    /**
     * 内外边距：用于多view时宽度计算：默认是多view通用边距
     */
    private int imageOutMargin = 20;
    private int imageInnerMargin = 16;
    private int bookNum = 3;

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public AdapterImageView(Context context) {
        this(context, null);
    }

    public AdapterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(attrs);
        initData();
    }

    private void initData() {
        blackZZWidth0 = DimensionPixelUtil.dip2px(mContext, 32);
        blackZZWidth1 = DimensionPixelUtil.dip2px(mContext, 18);

        backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.parseColor("#19000000"));

        blackZzPaint = new Paint();
        blackZzPaint.setStyle(Paint.Style.FILL);
        blackZzPaint.setAntiAlias(true);


        whiteZzPaint = new Paint();
        whiteZzPaint.setStyle(Paint.Style.FILL);
        whiteZzPaint.setAntiAlias(true);
    }

    private void initView(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AdapterImageView, 0, 0);
        imageMode = a.getInt(R.styleable.AdapterImageView_adapter_mode, IMGAEVIEW_MODE_COMMON);
        imageWidth = a.getInt(R.styleable.AdapterImageView_adapter_width, 0);
        imageHeight = a.getInt(R.styleable.AdapterImageView_adapter_height, 0);
        imageInnerMargin = a.getInt(R.styleable.AdapterImageView_adapter_inner_margin, 16);
        imageOutMargin = a.getInt(R.styleable.AdapterImageView_adapter_out_margin, 20);
        bookNum = a.getInt(R.styleable.AdapterImageView_adapter_num, 3);
        a.recycle();
        imageOutMargin = DimensionPixelUtil.dip2px(mContext, imageOutMargin);
        imageInnerMargin = DimensionPixelUtil.dip2px(mContext, imageInnerMargin);
    }

    /**
     * 宽度
     *
     * @param context
     * @return
     */
    public static int getWidthReturnInt(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenWidth = getWidthReturnInt(mContext);
        int imageViewWidh;
        int imageViewHeight;
        switch (imageMode) {
            case IMGAEVIEW_MODE_COMMON:
            default:
                break;
            case IMGAEVIEW_MODE_SINGLE:
                imageViewWidh = screenWidth * imageWidth / 360;
                imageViewHeight = imageViewWidh * imageHeight / imageWidth;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewWidh, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewHeight, MeasureSpec.EXACTLY);
                break;
            case IMGAEVIEW_MODE_MORE:
                int imageViewmargin = imageInnerMargin * (bookNum - 1) + imageOutMargin * 2;
                imageViewWidh = (screenWidth - imageViewmargin) / bookNum;
                imageViewHeight = imageViewWidh * imageHeight / imageWidth;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewWidh, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewHeight, MeasureSpec.EXACTLY);
                break;
            case IMGAEVIEW_MODE_MATCH:
                imageViewWidh = MeasureSpec.getSize(widthMeasureSpec);
                imageViewHeight = imageViewWidh * imageHeight / imageWidth;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewHeight, MeasureSpec.EXACTLY);
                break;
            case IMGAEVIEW_MODE_EXACTLY:
                imageViewHeight = DimensionPixelUtil.dip2px(mContext, imageHeight);
                imageViewWidh = DimensionPixelUtil.dip2px(mContext, imageWidth);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewWidh, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(imageViewHeight, MeasureSpec.EXACTLY);
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Paint backgroundPaint;
    private Paint whiteZzPaint;
    private Paint blackZzPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        if (isNeedBackground()) {
            drawBackground(canvas);
        }
        super.onDraw(canvas);
        if (whiteZZType != WHITE_ZZ_TYPE0) {
            drawWhiteZz(canvas);
        }
        if (position != BLACK_ZZ_POSITION0) {
            drawBlackZz(canvas);
        }
    }

    private int blackZZWidth0;
    private int blackZZWidth1;

    /**
     * 当前item距中心item的位置position
     */
    private int position;
    /**
     * 当前item在中心item的位置的左边还是右边 0：左边 1：右边
     */
    private int location;

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    private void drawBlackZz(Canvas canvas) {
        RectF blackZzRect = new RectF();
        blackZzRect.top = 0;
        blackZzRect.bottom = getMeasuredHeight();
        int viewWidth = getMeasuredWidth();
//        blackZzPaint.setColor(Color.parseColor("#ff0000"));
        //缩放后view宽度
        float viewScaleWidth = viewWidth * (1 - position * VIEWPAGER_SCALE);
        //缩放造成的距原leftx（未缩放view）上坐标距离
        float viewDis = (viewWidth - viewScaleWidth) / 2;
        //缩放后整个x上平移距离距原leftx（未缩放view）的坐标距离（即缩放后view嵌入距离）
        float viewQr = VIEWPAGER_MARGIN - viewDis;
        if (position == 2 || position == 3) {
            float lastViewScaleWidth = viewWidth * (1 - (position - 1) * VIEWPAGER_SCALE);
            float lastViewDis = (viewWidth - lastViewScaleWidth) / 2;
            viewQr = VIEWPAGER_MARGIN - viewDis - lastViewDis;
        }
        //view嵌入距离还原为原有距离
        float viewQrSource = viewQr / (1 - position * VIEWPAGER_SCALE);
        if (position == 3) {
            if (location == BLACK_ZZ_POSITIONRIGHT) {
                blackZzRect.left = viewWidth - viewQrSource - blackZZWidth1;
                blackZzRect.right = blackZzRect.left + blackZZWidth1;
            } else {
                blackZzRect.left = viewQrSource;
                blackZzRect.right = blackZzRect.left + blackZZWidth1;
            }
        } else {
            if (location == BLACK_ZZ_POSITIONRIGHT) {
                blackZzRect.left = viewWidth - viewQrSource - blackZZWidth0;
                blackZzRect.right = blackZzRect.left + blackZZWidth0;
            } else {
                blackZzRect.left = viewQrSource;
                blackZzRect.right = blackZzRect.left + blackZZWidth0;
            }
        }
        LinearGradient linearGradient = null;
        if (location == BLACK_ZZ_POSITIONLEFT) {
            linearGradient = new LinearGradient(blackZzRect.left, 0, blackZzRect.right, 0, new int[]{Color.parseColor("#4d292929"), Color.parseColor("#00ffffff")}, null, Shader.TileMode.CLAMP);
        } else {
            linearGradient = new LinearGradient(blackZzRect.left, 0, blackZzRect.right, 0, new int[]{Color.parseColor("#00ffffff"), Color.parseColor("#4d292929")}, null, Shader.TileMode.CLAMP);
        }
        blackZzPaint.setShader(linearGradient);
//        if (position == 2) {
//            LogUtils.e("blackZZWidth0", "location" + location + " &&&&&&viewWidth:" + viewWidth + " viewScaleWidth:"
//                    + viewScaleWidth + " viewDis:" + viewDis + " viewQr:" + viewQr
//                    + " viewQrSource:" + viewQrSource + " blackZzRect.left:" + blackZzRect.left + " blackZzRect.right:" + blackZzRect.right);
//        }
        canvas.drawRoundRect(blackZzRect, 0, 0, blackZzPaint);
    }

    public final static int WHITE_ZZ_TYPE0 = 0;
    public final static int WHITE_ZZ_TYPE1 = 1;
    public final static int WHITE_ZZ_TYPE2 = 2;
    public final static int WHITE_ZZ_TYPE3 = 3;

    public final static int BLACK_ZZ_POSITION0 = 0;
    public final static int BLACK_ZZ_POSITION1 = 1;
    public final static int BLACK_ZZ_POSITION2 = 2;
    public final static int BLACK_ZZ_POSITION3 = 3;

    public final static int BLACK_ZZ_POSITIONLEFT = 0;
    public final static int BLACK_ZZ_POSITIONRIGHT = 1;


    public final static float VIEWPAGER_SCALE = 0.085f;
    public final static float VIEWPAGER_MARGIN = 145f;

    private void drawWhiteZz(Canvas canvas) {
        RectF whiteZzRect = new RectF();
        whiteZzRect.left = 0;
        whiteZzRect.top = 0;
        whiteZzRect.right = getMeasuredWidth();
        whiteZzRect.bottom = getMeasuredHeight();
        if (whiteZZType == WHITE_ZZ_TYPE1) {
            whiteZzPaint.setColor(Color.parseColor("#33ffffff"));
        } else if (whiteZZType == WHITE_ZZ_TYPE2) {
            whiteZzPaint.setColor(Color.parseColor("#66ffffff"));
        } else if (whiteZZType == WHITE_ZZ_TYPE3) {
            whiteZzPaint.setColor(Color.parseColor("#99ffffff"));
        }
        canvas.drawRoundRect(whiteZzRect, mBorderRadius, mBorderRadius, whiteZzPaint);
    }

    /**
     * 遮罩类型：0：不需要 1：20%白遮罩 2：40%白遮罩 3：60%白遮罩
     */
    private int whiteZZType = 0;

    public void setWhiteZZType(int whiteZZType) {
        this.whiteZZType = whiteZZType;
        invalidate();
    }

    protected boolean isNeedBackground() {
        return true;
    }

    private void drawBackground(Canvas canvas) {
        RectF backgroundRect = new RectF();
        backgroundRect.left = 0;
        backgroundRect.top = 0;
        backgroundRect.right = getMeasuredWidth();
        backgroundRect.bottom = getMeasuredHeight();
        canvas.drawRoundRect(backgroundRect, mBorderRadius, mBorderRadius, backgroundPaint);
    }

    public void setMarginSize(int inner, int outer) {
        imageOutMargin = DimensionPixelUtil.dip2px(mContext, outer);
        imageInnerMargin = DimensionPixelUtil.dip2px(mContext, inner);
    }

    public void setAdapterScale(int width, int height) {
        imageWidth = width;
        imageHeight = height;
    }

    public void setMode(int mode) {
        imageMode = mode;
    }
}
