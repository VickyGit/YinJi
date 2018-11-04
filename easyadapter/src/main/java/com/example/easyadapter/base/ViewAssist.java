package com.example.easyadapter.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

public interface ViewAssist {
//    View相关
    public EasyRvViewHolder setText(int viewId,String text);
    public EasyRvViewHolder setBitmap(int viewId, Bitmap bitmap);
    public EasyRvViewHolder setChecked(int viewId,boolean checked);
    public EasyRvViewHolder setImageResource(int viewId,int resId);
    public EasyRvViewHolder setVisible(int viewId,int visibility);
    public EasyRvViewHolder setBackground(int viewId,int color);
//    事件相关
    public EasyRvViewHolder setOnClickListener(int viewId, View.OnClickListener listener);
    public EasyRvViewHolder setOnLongClickListener(int viewId,View.OnLongClickListener listener);
    public EasyRvViewHolder setOnTouchListener(int viedId,View.OnTouchListener listener);
}
