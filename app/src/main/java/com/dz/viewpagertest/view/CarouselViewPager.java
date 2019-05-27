package com.dz.viewpagertest.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import com.dz.viewpagertest.Utils.LogUtils;

/**
 * 旋转木马效果的ViewPager
 * Created by lishaojie on 2018/4/16.
 */
public class CarouselViewPager extends ViewPager {
    public CarouselViewPager(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        //需要动修复
        int currentItem = getCurrentItem();
//        LogUtils.e("getChildDrawingOrder0", "i:" + i + " currentItem: " + currentItem);
        if (i >= currentItem) {
            int position = childCount - 1 - i + currentItem;
//            LogUtils.e("getChildDrawingOrder0", "i:" + i +
//                    " currentItem: " + currentItem + " position: " + position);
            return position;
        }
        return super.getChildDrawingOrder(childCount, i);
    }
}
