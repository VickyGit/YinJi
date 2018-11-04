package com.example.vicky.yinji.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vicky.yinji.R;
import com.example.vicky.yinji.entry.Diary;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private List<Diary> mData;

    public DiaryAdapter(Context mContext, List<Diary> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DiaryViewHolder viewHolder=new DiaryViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_diary,viewGroup,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder diaryViewHolder, int i) {
        Glide.with(mContext).load(R.mipmap.ic_image).into(diaryViewHolder.ivBg);
        diaryViewHolder.tvTitle.setText(mData.get(i).title);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivBg;
        public TextView tvTitle;
        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBg=itemView.findViewById(R.id.iv_bg);
            tvTitle=itemView.findViewById(R.id.tv_title);
        }
    }

    public void changeTitle(){
        for (int i=0;i<mData.size();i++){
            Diary diary=mData.get(i);
            diary.title="修改后的值:"+i;
        }
        notifyDataSetChanged();
    }
}
