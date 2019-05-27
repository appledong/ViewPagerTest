package com.dz.viewpagertest;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dz.viewpagertest.view.AdapterImageView;
import com.dz.viewpagertest.view.CarouselTransformer;
import com.dz.viewpagertest.view.CarouselViewPager;
import com.dz.viewpagertest.view.ItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CarouselViewPager viewPager;

    private List<Integer> urls;

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        setListener();
    }

    private int selectedPosition = 3;

    private void setListener() {
        findViewById(R.id.linearlayoutroot).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                referenceItemZz(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {//滑动停止
                    if (selectedPosition == 2) {
                        viewPager.setCurrentItem(urls.size() - 4, false);
                    } else if (selectedPosition == urls.size() - 3) {
                        viewPager.setCurrentItem(3, false);
                    }
                }
            }
        });
    }

    /**
     * 刷新itemview的遮罩显示
     *
     * @param position
     */
    private void referenceItemZz(int position) {
        int count = viewPager.getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                View view = viewPager.getChildAt(i);
                if (view != null && view instanceof ItemView) {
                    ItemView itemView = (ItemView) view;
                    int zzType = -10;
                    int blackPosition = -10;
                    int blackLocation = -10;
                    if (i == position) {
                        zzType = AdapterImageView.WHITE_ZZ_TYPE0;
                        blackPosition = AdapterImageView.BLACK_ZZ_POSITION0;
                    } else if (position == i - 1 || position == i + 1) {
                        zzType = AdapterImageView.WHITE_ZZ_TYPE1;
                        blackPosition = AdapterImageView.BLACK_ZZ_POSITION1;
                        if (position == i - 1) {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONLEFT;
                        } else {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONRIGHT;
                        }
                    } else if (position == i - 2 || position == i + 2) {
                        zzType = AdapterImageView.WHITE_ZZ_TYPE2;
                        blackPosition = AdapterImageView.BLACK_ZZ_POSITION2;
                        if (position == i - 2) {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONLEFT;
                        } else {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONRIGHT;
                        }
                    } else if (position == i - 3 || position == i + 3) {
                        zzType = AdapterImageView.WHITE_ZZ_TYPE3;
                        blackPosition = AdapterImageView.BLACK_ZZ_POSITION3;
                        if (position == i - 3) {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONLEFT;
                        } else {
                            blackLocation = AdapterImageView.BLACK_ZZ_POSITIONRIGHT;
                        }
                    }
                    itemView.setWhiteZZType(zzType);
                    itemView.setLocation(blackLocation);
                    itemView.setPosition(blackPosition);
                }
            }
        }
    }

    private void initData() {
        urls = new ArrayList<>();
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
        urls.add(R.drawable.pic4);
        urls.add(R.drawable.pic1);
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
        urls.add(R.drawable.pic4);
        urls.add(R.drawable.pic1);
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
        urls.add(R.drawable.pic4);
        urls.add(R.drawable.pic1);
        urls.add(R.drawable.pic2);
        urls.add(R.drawable.pic3);
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPageTransformer(false, new CarouselTransformer(AdapterImageView.VIEWPAGER_SCALE, -AdapterImageView.VIEWPAGER_MARGIN));
        viewPager.setOffscreenPageLimit(urls.size());
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(selectedPosition);
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return urls.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ItemView itemView = new ItemView(container.getContext());
            itemView.bindData(urls.get(position));
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
