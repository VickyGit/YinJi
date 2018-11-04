package com.example.easyadapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

public class EasyRvViewHolder extends RecyclerView.ViewHolder implements ViewAssist{
    public SparseArrayCompat<View> mViews;
    private Context mContext;
    public View mConverView;

    private EasyRvViewHolder(Context context,@NonNull View itemView) {
        super(itemView);
        mContext=context;
        mConverView=itemView;
        mViews=new SparseArrayCompat<>();
    }

    /**
     * 获取ViewHolder
     * @param mContext
     * @param parent
     * @param layoutId
     * @return
     */
    public static EasyRvViewHolder get(Context mContext,ViewGroup parent,int layoutId){
        View itemView=LayoutInflater.from(mContext).inflate(layoutId,parent,false);
        EasyRvViewHolder viewHolder=new EasyRvViewHolder(mContext,itemView);
        return viewHolder;
    }

    public <T extends View> T getView(int layoutId){
        View view=mViews.get(layoutId);
        if (view==null){
            view=mConverView.findViewById(layoutId);
            mViews.put(layoutId,view);
        }
        return (T) view;
    }

    @Override
    public EasyRvViewHolder setText(int viewId, String text) {
        TextView textView=getView(viewId);
        textView.setText(text);
        return this;
    }

    @Override
    public EasyRvViewHolder setBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView=getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    @Override
    public EasyRvViewHolder setChecked(int viewId, boolean checked) {
        Checkable checkable=getView(viewId);
        checkable.setChecked(checked);
        return this;
    }

    @Override
    public EasyRvViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView=getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    @Override
    public EasyRvViewHolder setVisible(int viewId, int visibility) {
        View view=getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    @Override
    public EasyRvViewHolder setBackground(int viewId,int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }


    @Override
    public EasyRvViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    @Override
    public EasyRvViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    @Override
    public EasyRvViewHolder setOnTouchListener(int viedId, View.OnTouchListener listener) {
        getView(viedId).setOnTouchListener(listener);
        return this;
    }


    public View getConverView(){
        return this.mConverView;
    }

}
