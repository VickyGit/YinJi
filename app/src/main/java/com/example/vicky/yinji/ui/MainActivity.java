package com.example.vicky.yinji.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyadapter.base.EasyOnLoadMoreListener;
import com.example.easyadapter.base.EasyRvItemListenter;
import com.example.easyadapter.base.EasyRvViewHolder;
import com.example.easyadapter.base.FooterListener;
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
    private TestAdapter mAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mAdapter.setLoadingState(mAdapter.LOADING_COMPLETE);
                    mAdapter.setLoadingState(mAdapter.LOADING_END);
                    mAdapter.addData(addData());
                    break;
            }
        }
    };

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
//        mAdapter=new DiaryAdapter(this,mDiaryData);
//        rvDiary.setLayoutManager(new LinearLayoutManager(this));
//        rvDiary.setAdapter(mAdapter);
        mAdapter=new TestAdapter(this,R.layout.item_diary,mDiaryData);
        mAdapter.setOnItemClickListener(new EasyRvItemListenter.onItemClickListener<Diary>() {
            @Override
            public void onItemClick(Diary item, int position, View view) {
                Toast.makeText(mActivity,"点击了:"+position+"标题是:"+item.title,Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemLongClickListener(new EasyRvItemListenter.onItemLongClickListener<Diary>() {
            @Override
            public void onItemLongClick(Diary item, int position, View view) {
                Toast.makeText(mActivity,"长点击了:"+position+"标题是:"+item.title,Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.addFootLayout(R.layout.item_footer, new FooterListener() {
            @Override
            public void loadingComplite(EasyRvViewHolder easyRvViewHolder) {
                easyRvViewHolder.getConverView().setVisibility(View.GONE);
                easyRvViewHolder.setText(R.id.tv_footer,"加载完成");
            }

            @Override
            public void loading(EasyRvViewHolder easyRvViewHolder) {
                easyRvViewHolder.getConverView().setVisibility(View.VISIBLE);
                easyRvViewHolder.setText(R.id.tv_footer,"正在加载更多....");
            }

            @Override
            public void loadingEnd(EasyRvViewHolder easyRvViewHolder) {
                easyRvViewHolder.getConverView().setVisibility(View.VISIBLE);
                easyRvViewHolder.setText(R.id.tv_footer,"我也是有底线的");
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rvDiary.setLayoutManager(linearLayoutManager);
        rvDiary.setAdapter(mAdapter);
        rvDiary.addOnScrollListener(new EasyOnLoadMoreListener() {
            @Override
            public void loadMore() {
                int count=0;
                if (count<=5){
                    mAdapter.setLoadingState(mAdapter.LOADING);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessageDelayed(1,5000);
                        }
                    }).start();
                    count++;
                }

            }
        });
    }

    public void initData(){
        for (int i=1;i<=30;i++){
            Diary diary=new Diary();
            diary.imgUrl=R.mipmap.ic_image;
            diary.title="标题"+i;
            mDiaryData.add(diary);
        }
    }

    public List<Diary> addData(){
        List<Diary> addData=new ArrayList<>();
        for (int i=1;i<=30;i++){
            Diary diary=new Diary();
            diary.imgUrl=R.mipmap.ic_image;
            diary.title="新增标题"+i;
            addData.add(diary);
        }
        return addData;
    }

}
