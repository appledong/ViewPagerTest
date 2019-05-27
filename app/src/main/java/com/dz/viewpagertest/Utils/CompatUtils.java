package com.dz.viewpagertest.Utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import java.util.Locale;

/**
 * 色值解析
 *
 * @author Created by wxliao on 18/4/28.
 */

public class CompatUtils {
    /**
     * 获取color
     *
     * @param context  上下文
     * @param colorRes 资源
     * @return int
     */
    public static int getColor(@NonNull Context context, @ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    /**
     * 获取drawable
     *
     * @param context     上下文
     * @param drawableRes 资源
     * @return Drawable
     */
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(context, drawableRes);
    }

    /**
     * 获取color list
     *
     * @param context 上下文
     * @param id      资源
     * @return ColorStateList
     */
    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int id) {
        return ContextCompat.getColorStateList(context, id);
    }

    /**
     * 获取Locale
     *
     * @param configuration 配置
     * @return Locale
     */
    public static Locale getLocale(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList list = configuration.getLocales();
            if (!list.isEmpty()) {
                return list.get(0);
            }
        }
        return configuration.locale;
    }

    /**
     * 获取size
     *
     * @param context 上下文
     * @return Point
     */
    public static Point getSize(Context context) {
        if (null != context) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (null != wm) {
                Point point = new Point();
                wm.getDefaultDisplay().getSize(point);
                return point;
            }
        }
        return null;
    }

    /**
     * 获取color
     *
     * @param view     视图
     * @param drawable 资源
     */
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        view.setBackground(drawable);
    }
}
