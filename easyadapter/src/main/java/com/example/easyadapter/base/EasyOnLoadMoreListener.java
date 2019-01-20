package com.example.easyadapter.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EasyOnLoadMoreListener extends RecyclerView.OnScrollListener {
    //用来标记是否正在向上滑动
    private boolean isSlidingUpward=false;
    private boolean canLoadMore=true;

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager manager= (LinearLayoutManager) recyclerView.getLayoutManager();
            if (newState==RecyclerView.SCROLL_STATE_IDLE){
                int lastItemPosition=manager.findLastCompletelyVisibleItemPosition();
                int itemCount=manager.getItemCount();
                if (canLoadMore && lastItemPosition == (itemCount-1) &&isSlidingUpward){
                    loadMore();
                }
            }
        }else if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (newState==RecyclerView.SCROLL_STATE_IDLE){
                int lastItemPosition=manager.findLastCompletelyVisibleItemPosition();
                int itemCount=manager.getItemCount();
                if (canLoadMore && lastItemPosition == (itemCount-1) && isSlidingUpward){
                    loadMore();
                }
            }
        }

    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isSlidingUpward = dy>0;
    }

    public abstract void loadMore();
}
