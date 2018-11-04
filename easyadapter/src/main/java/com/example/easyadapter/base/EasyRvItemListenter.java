package com.example.easyadapter.base;

import android.view.View;

public interface EasyRvItemListenter{
    interface onItemClickListener<T>{
        void onItemClick(T item,int position,View view);
    }
    interface onItemLongClickListener<T>{
        void onItemLongClick(T item,int position,View view);
    }
}
