package com.example.vicky.yinji.ui;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.easyadapter.base.EasyRvAdapter;
import com.example.easyadapter.base.EasyRvViewHolder;
import com.example.easyadapter.base.FooterListener;
import com.example.vicky.yinji.R;
import com.example.vicky.yinji.entry.Diary;

import java.util.List;

public class DiaryAdapter extends EasyRvAdapter<Diary> {
    public DiaryAdapter(Context mContext, int mLayoutId, List<Diary> mDatas) {
        super(mContext, mLayoutId, mDatas);
    }

    @Override
    public void convert(EasyRvViewHolder easyRvViewHolder, Diary diary, int position) {
        if (position<mDatas.size()){
            Glide.with(mContext).load(R.mipmap.ic_image).into((ImageView) easyRvViewHolder.getView(R.id.iv_bg));
            easyRvViewHolder.setText(R.id.tv_title,diary.title);
        }
    }

    @Override
    public void addFootLayout(int footLayoutId, FooterListener footerListener) {
        super.addFootLayout(footLayoutId, footerListener);
    }

    public void setTitle(){
        for (int i=0;i<mDatas.size();i++){
            Diary diary=mDatas.get(i);
            diary.title="修改后的值:"+i;
        }
    }
}
