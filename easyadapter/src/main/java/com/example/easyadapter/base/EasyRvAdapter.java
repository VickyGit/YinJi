package com.example.easyadapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easyadapter.R;

import java.util.List;

public abstract class EasyRvAdapter<T> extends RecyclerView.Adapter<EasyRvViewHolder>{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mFootLayoutId = -1;
    protected boolean mHasFooter=false;
    private int mLoadingState=1;
    public final int LOADING=1;
    public final int LOADING_COMPLETE=2;
    public final int LOADING_END=3;
    protected FooterListener mFooterListener;
//    item类型
    protected final int TYPE_NORMAL=0;
    protected final int TYPE_FOOTER=1;

//    点击事件
    protected EasyRvItemListenter.onItemClickListener<T> onItemClickListener;
    protected EasyRvItemListenter.onItemLongClickListener<T> onItemLongClickListener;

    private EasyRvViewHolder mFooter=null;
    private View.OnClickListener mOnClickListner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag(R.id.tag_position);
            T item= (T) v.getTag(R.id.tag_item);
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(item,position,v);
            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener= new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position= (int) v.getTag(R.id.tag_position);
            T item= (T) v.getTag(R.id.tag_item);
            if (onItemLongClickListener!=null){
                onItemLongClickListener.onItemLongClick(item,position,v);
            }
            return true;
        }
    };


    public EasyRvAdapter(Context mContext, int mLayoutId, List<T> mDatas) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public EasyRvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType==TYPE_FOOTER){
            EasyRvViewHolder easyRvViewHolder=EasyRvViewHolder.get(mContext,viewGroup,mFootLayoutId);
            //解决调用notifyDataSetChanged时,width变成wrap_content的问题
            if (easyRvViewHolder.getConverView().getLayoutParams ().width == RecyclerView.LayoutParams.MATCH_PARENT){
                easyRvViewHolder.getConverView().getLayoutParams ().width = viewGroup.getWidth();
            }
            if (easyRvViewHolder.getConverView().getLayoutParams().height == RecyclerView.LayoutParams.MATCH_PARENT){
                easyRvViewHolder.getConverView().getLayoutParams().height= viewGroup.getHeight();
            }
            return easyRvViewHolder;
        }else{
            EasyRvViewHolder easyRvViewHolder=EasyRvViewHolder.get(mContext,viewGroup,mLayoutId );
            return easyRvViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EasyRvViewHolder easyRvViewHolder, int position) {
        if (mHasFooter && position==mDatas.size()){
            switch (mLoadingState){
                case LOADING:
                    mFooterListener.loading(easyRvViewHolder);
                    break;
                case LOADING_COMPLETE:
                    mFooterListener.loadingComplite(easyRvViewHolder);
                    break;
                case LOADING_END:
                    mFooterListener.loadingEnd(easyRvViewHolder);
                    break;
            }
        }else {
            easyRvViewHolder.getConverView().setTag(R.id.tag_position,position);
            easyRvViewHolder.getConverView().setTag(R.id.tag_item,mDatas.get(position));
            easyRvViewHolder.getConverView().setOnClickListener(mOnClickListner);
            easyRvViewHolder.getConverView().setOnLongClickListener(mOnLongClickListener);
            convert(easyRvViewHolder,mDatas.get(position),position);
        }
    }


    @Override
    public int getItemCount() {
        return mHasFooter?mDatas.size()+1:mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position==mDatas.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_NORMAL;
        }
    }

    public abstract void convert(EasyRvViewHolder easyRvViewHolder, T t,int position);

    public void addFootLayout(int footLayoutId,FooterListener footerListener){
        this.mFootLayoutId=footLayoutId;
        if (footerListener!=null){
            this.mFooterListener=footerListener;
        }
        mHasFooter=true;
        notifyDataSetChanged();
    }

    public void setLoadingState(int loadingState) {
        this.mLoadingState = loadingState;
        notifyDataSetChanged();
    }

    public void setmFooterListener(FooterListener mFooterListener) {
        this.mFooterListener = mFooterListener;
    }

    public boolean addData(List<T> newData){
        if (newData!=null && newData.size()>0){
            boolean result =mDatas.addAll(newData);
            notifyDataSetChanged();
            return result;
        }else{
            return false;
        }
    }

    public void setOnItemLongClickListener(EasyRvItemListenter.onItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(EasyRvItemListenter.onItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager=
                recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果当前是footer,占据多行
                    return getItemViewType(position) == TYPE_FOOTER? gridLayoutManager.getSpanCount():1;
                }
            });
        }
    }
}
