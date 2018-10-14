package com.example.vicky.yinji.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.vicky.yinji.R;
import com.example.vicky.yinji.base.BaseAppCompatActivity;
import com.example.vicky.yinji.entry.Diary;
import com.example.vicky.yinji.widget.MenuPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseAppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_diary)
    RecyclerView rvDiary;
    @BindView(R.id.fab)
    FloatingActionButton fabButton;
    private List<Diary> mDiaryData=new ArrayList<>();
    private DiaryAdapter mAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        toolbar.setTitle("印迹");
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuPopupWindow menuPopupWindow=new MenuPopupWindow(mActivity);
                menuPopupWindow.showPopWindow(toolbar);
            }
        });
        mAdapter=new DiaryAdapter(this,mDiaryData);
        rvDiary.setLayoutManager(new LinearLayoutManager(this));
        rvDiary.setAdapter(mAdapter);
    }

    public void initData(){
        for (int i=0;i<30;i++){
            Diary diary=new Diary();
            diary.imgUrl=R.mipmap.ic_image;
            diary.title="胡湛韵是猪"+i;
            mDiaryData.add(diary);
        }
    }
}
