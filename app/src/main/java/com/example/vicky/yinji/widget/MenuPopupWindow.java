package com.example.vicky.yinji.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vicky.yinji.R;
import com.example.vicky.yinji.util.FastBlurUtil;
import com.example.vicky.yinji.util.MenuAnimatorEvaluator;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MenuPopupWindow extends PopupWindow implements View.OnClickListener {
    private Activity mContext;
    private int statusBarHeight ;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap= null;
    private Bitmap overlay = null;



    public MenuPopupWindow(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 计算屏幕的大小,初始化宽高
     */
    public void init(){
        Rect rect=new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        statusBarHeight = rect.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth=metrics.widthPixels;
        mHeight=metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);
    }

    /**
     * 模糊背景图
     * @return
     */
    private Bitmap blur(){
        if (overlay!=null){
            return overlay;
        }
        //同个DecorView截图屏幕
        View view=mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap= view.getDrawingCache();

        float scaleFactor = 8;//图片缩放比例；
        float radius = 10;//模糊程度
        int width = mBitmap.getWidth();
        int height =  mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor),(int) (height / scaleFactor),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlurUtil.doBlur(overlay, (int) radius, true);
        return overlay;
    }

    public void showPopWindow(View anchor){
        init();
        ConstraintLayout layout= (ConstraintLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_menu,null);
        setContentView(layout);
        TextView tvSkip=layout.findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(this);
        final Button btnAlbum=layout.findViewById(R.id.btn_from_album);
        btnAlbum.setOnClickListener(this);
        final Button btnPhoto=layout.findViewById(R.id.btn_from_photo);
        btnPhoto.setOnClickListener(this);
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    for (int i=0;i<2;i++){
                        subscriber.onNext(i);
                        Thread.sleep(300);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer){
                            case 0:
                                startBtnAnim(btnAlbum);
                                break;
                            case 1:
                                startBtnAnim(btnPhoto);
                                break;
                        }
                    }
                });
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_skip:
                Toast.makeText(mContext,"点击了跳过",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btn_from_album:
                Toast.makeText(mContext,"点击了从相册选择",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btn_from_photo:
                Toast.makeText(mContext,"点击了从拍照",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    public void startBtnAnim(View view){
        int translationY=mHeight-view.getBottom();
        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(view,"translationY",translationY,0);
        objectAnimator.setDuration(1000);
        MenuAnimatorEvaluator evaluator=new MenuAnimatorEvaluator();
        evaluator.setDuration(1000);
        objectAnimator.setEvaluator(evaluator);
        objectAnimator.start();
        view.setVisibility(View.VISIBLE);
    }

}
