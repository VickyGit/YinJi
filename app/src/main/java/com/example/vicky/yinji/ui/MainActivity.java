package com.example.vicky.yinji.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easyadapter.base.EasyOnLoadMoreListener;
import com.example.easyadapter.base.EasyRvItemListenter;
import com.example.easyadapter.base.EasyRvViewHolder;
import com.example.easyadapter.base.FooterListener;
import com.example.vicky.yinji.R;
import com.example.vicky.yinji.base.BaseAppCompatActivity;
import com.example.vicky.yinji.entry.Diary;
import com.example.vicky.yinji.ui.readDiary.ReadDiaryActivity;
import com.example.vicky.yinji.util.DensityUtil;
import com.example.vicky.yinji.widget.MenuPopupWindow;

import java.io.File;
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
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    private List<Diary> mDiaryData=new ArrayList<>();
    private DiaryAdapter mAdapter;
    private File mCameraFile;
    private File mZoomFile;
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
                menuPopupWindow.setListener(new MenuPopupWindow.onMenuListener() {
                    @Override
                    public void clickFromCamera() {
                        chooseFromCamera();
                    }

                    @Override
                    public void clickFromGallery() {
                        chooseFromGallery();
                    }
                });
                menuPopupWindow.showPopWindow(toolbar);
            }
        });

        mAdapter=new DiaryAdapter(this,R.layout.item_diary,mDiaryData);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        switch (requestCode){
            case CAMERA_CODE:
                if (Build.VERSION.SDK_INT>=24){
                    startImageZoom(FileProvider.getUriForFile(this, "com.example.vicky.yinji.provider", mCameraFile));
                }else {
                    startImageZoom(Uri.fromFile(mCameraFile));
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else {
                    Uri imageUri = data.getData();
                    startImageZoom(imageUri);
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else {
                    Intent intent = new Intent(this, ReadDiaryActivity.class);
                    intent.putExtra("imageUri",Uri.fromFile(mZoomFile));
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }




    /**
     * 拍照选择图片
     */
    public void chooseFromCamera() {
        mCameraFile = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".png");
        try {
            if (mCameraFile.exists()) {
                mCameraFile.delete();
            }
            mCameraFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.example.vicky.yinji.provider", mCameraFile);
        } else {
            imageUri = Uri.fromFile(mCameraFile);
        }
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        //调用系统相机
        this.startActivityForResult(intent, CAMERA_CODE);
    }


    /**
     * 从相册选择图片
     */
    public void chooseFromGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent, GALLERY_CODE);
    }


    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        try{
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", DensityUtil.dip2px(this,200));
            intent.putExtra("outputY", DensityUtil.dip2px(this,200));
            intent.putExtra("scale", true);
            //将剪切的图片保存到目标Uri中
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            //由于intent大小限制，所以直接将裁剪后的图片以文件形式输出
            mZoomFile = new File(this.getExternalCacheDir(), System.currentTimeMillis() + "-crop.png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mZoomFile));
            this.startActivityForResult(intent, CROP_CODE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
