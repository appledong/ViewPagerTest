package com.dz.viewpagertest.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 代码中的单位转换 px dip sp
 *
 * @author perry
 */
public class DimensionPixelUtil {
    public final static int PX = TypedValue.COMPLEX_UNIT_PX;
    public final static int DIP = TypedValue.COMPLEX_UNIT_DIP;
    public final static int SP = TypedValue.COMPLEX_UNIT_SP;

    private static int dip2px5 = -1;
    private static int dip2px2 = -1;

    /**
     * @param unit    单位 </br>0 px</br>1 dip</br>2 sp
     * @param value   size 大小
     * @param context
     * @return
     */
    public static float getDimensionPixelSize(int unit, float value, Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        switch (unit) {
            case PX:
                return value;
            case DIP:
            case SP:
                return TypedValue.applyDimension(unit, value, metrics);
            default:
                throw new IllegalArgumentException("unknow unix");
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的屏幕属性从 dip 的单位 转成为 px(像素)
     *
     * @param context context
     * @param value   float 类型
     * @return
     */
    public static float dip2px(Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return value * metrics.density;
    }

    /**
     * 根据手机的屏幕属性从 dip 的单位 转成为 px(像素)
     *
     * @param context context
     * @param value   int 类型
     * @return
     */
    public static int dip2px(Context context, int value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (value * metrics.density);
    }

    /**
     * 根据手机的屏幕属性从 dip 的单位 转成为 px(像素)
     *
     * @param context
     * @return
     */
    public static int dip2px_5(Context context) {
        if (dip2px5 > 0) {
            return dip2px5;
        }

        dip2px5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
        return dip2px5;
    }

    /**
     * 根据手机的屏幕属性从 dip 的单位 转成为 px(像素)
     *
     * @param context
     * @return
     */
    public static int dip2px_2(Context context) {
        if (dip2px2 > 0) {
            return dip2px2;
        }

        dip2px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        return dip2px2;
    }
}
