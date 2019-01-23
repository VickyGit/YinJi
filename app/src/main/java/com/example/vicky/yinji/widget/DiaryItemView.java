package com.example.vicky.yinji.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vicky.yinji.R;

public class DiaryItemView extends LinearLayout {
    private Context mContext;
    private Button mBtnDelete;
    private Button mBtnEdit;
    private ImageView mIvMenu;
    private ImageView mIvBg;
    private TextView mTvTitle;
    private ConstraintLayout mClMenu;
    private OnMenuListener mMenuListener;
    private ObjectAnimator mShowAnimation;
    private ObjectAnimator mHideAnimation;

    public DiaryItemView(Context context) {
        super(context);
        init(context);
    }

    public DiaryItemView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DiaryItemView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public OnMenuListener getMenuListener() {
        return mMenuListener;
    }

    public void setMenuListener(OnMenuListener menuListener) {
        this.mMenuListener = menuListener;
    }

    /**
     * 初始化
     */
    private void init(Context context){
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_diary,this,true);
        mBtnDelete = view.findViewById(R.id.btn_delete);
        mBtnEdit = view.findViewById(R.id.btn_edit);
        mIvMenu = view.findViewById(R.id.iv_menu);
        mClMenu = view.findViewById(R.id.cl_menu);
        mIvBg = view.findViewById(R.id.iv_bg);
        mTvTitle = view.findViewById(R.id.tv_title);
        mBtnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuListener != null){
                    mMenuListener.onClickDelete();
                }
            }
        });
        mBtnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuListener != null){
                    mMenuListener.onClickEdit();
                }
            }
        });
        mIvMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClMenu.getVisibility() == VISIBLE){
                    hideMenu();
                }else {
                    showMenu();
                }
            }
        });
        mClMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });

        initAnim();
    }

    /**
     * 初始化动画
     */
    private void initAnim(){
        mShowAnimation = ObjectAnimator.ofFloat(mClMenu,"alpha",0,1);
        mShowAnimation.setDuration(300);
        mShowAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mClMenu.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        mHideAnimation = ObjectAnimator.ofFloat(mClMenu,"alpha",1,0);
        mHideAnimation.setDuration(300);
        mHideAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mClMenu.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mClMenu.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 显示菜单的动画
     */
    public void showMenu(){
        mShowAnimation.start();
    }

    /**
     * 隐藏菜单的动画
     */
    public void hideMenu(){
        mHideAnimation.start();
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        if (mTvTitle!=null) {
            mTvTitle.setText(title);
        }
    }

    /**
     * 设置背景图片
     * @param resourceId
     */
    public void setBackGround(Integer resourceId){
        if (mIvBg!=null){
            Glide.with(mContext).load(resourceId).into(mIvBg);
        }
    }

    public interface OnMenuListener{
        void onClickDelete();
        void onClickEdit();
    }
}
