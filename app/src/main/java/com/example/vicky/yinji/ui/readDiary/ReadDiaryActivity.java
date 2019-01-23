package com.example.vicky.yinji.ui.readDiary;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vicky.yinji.R;
import com.example.vicky.yinji.base.BaseAppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class ReadDiaryActivity extends BaseAppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private Uri mZoomUri;
    private String mTime;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_read_diary;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        mTime = simpleDateFormat.format(date);
        toolbar.setTitle(mTime);
        mZoomUri = getIntent().getParcelableExtra("imageUri");
        Glide.with(this).load(mZoomUri).into(ivHeader);
    }

    @Override
    protected void initData() {

    }
}
