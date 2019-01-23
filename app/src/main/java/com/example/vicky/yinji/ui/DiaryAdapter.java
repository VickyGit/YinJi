package com.example.vicky.yinji.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.easyadapter.base.EasyRvAdapter;
import com.example.easyadapter.base.EasyRvViewHolder;
import com.example.easyadapter.base.FooterListener;
import com.example.vicky.yinji.R;
import com.example.vicky.yinji.entry.Diary;
import com.example.vicky.yinji.widget.DiaryItemView;

import java.util.List;

public class DiaryAdapter extends EasyRvAdapter<Diary> {
    public DiaryAdapter(Context mContext, int mLayoutId, List<Diary> mDatas) {
        super(mContext, mLayoutId, mDatas);
    }

    @Override
    public void convert(EasyRvViewHolder easyRvViewHolder, Diary diary, int position) {
        if (position<mDatas.size()){
            DiaryItemView diaryItemView = easyRvViewHolder.getView(R.id.diary_item);
            diaryItemView.setTitle(diary.title);
            diaryItemView.setBackGround(R.mipmap.ic_image);
            diaryItemView.setMenuListener(new DiaryItemView.OnMenuListener() {
                @Override
                public void onClickDelete() {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("亲爱的主人，你确定要删除这条印迹吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"你点击了确定",Toast.LENGTH_LONG).show();
                                }
                            }).show();
                }

                @Override
                public void onClickEdit() {

                }
            });

        }
    }

    @Override
    public void addFootLayout(int footLayoutId, FooterListener footerListener) {
        super.addFootLayout(footLayoutId, footerListener);
    }

}
