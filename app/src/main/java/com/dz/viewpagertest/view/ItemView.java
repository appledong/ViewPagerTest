package com.dz.viewpagertest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.dz.viewpagertest.R;

public class ItemView extends RelativeLayout {

    private Context mContext;

    private AdapterImageView imageViewSource;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
    }

    private void initData() {

    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_item, this);
        imageViewSource = view.findViewById(R.id.imageview);
    }

    public void bindData(int url) {
        imageViewSource.setImageResource(url);
    }

    public void setWhiteZZType(int zzType) {
        if (imageViewSource != null) {
            imageViewSource.setWhiteZZType(zzType);
        }
    }

    public void setPosition(int position) {
        if (imageViewSource != null) {
            imageViewSource.setPosition(position);
        }
    }

    public void setLocation(int location) {
        if (imageViewSource != null) {
            imageViewSource.setLocation(location);
        }
    }

}
