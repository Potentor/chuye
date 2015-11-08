package com.hulian.firstpage.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ListView;

import com.hulian.firstpage.R;
import com.loopj.android.image.SmartImageView;
import com.nineoldandroids.view.ViewHelper;


/**
 * 图片背景view
 */
public final class WordBackView extends SmartImageView {

    private static final int edgeColorSelect=Color.RED;
    private static final int edgeColorUnselect=Color.YELLOW;
    private WordControlView friendView;
    private boolean isEditAble=true;
    private float oldX,oldY;
    private float transX,transY;
    private float mCurX,mCurY;
    private float centerX,centerY;
    private float SIDELENGTH;
//  private OnChanedListener mOnChanedListener;
    private WordViewBuilder.OnChanedListener mOnChanedListener;
//  private long oldTime;
    private MOnClickLisnstener mOnClickLisnstener;
    private boolean isCancelClickEnable=true;
    private float minMove;
 //   private float strokeWidth=1;//背景边框线条宽
    private int strokeColor=edgeColorUnselect;//背景边框线条颜色;
    private boolean isFirstLoad=true;
    private  int  ORIGINSLWIDTH;//原始宽
    private  int  ORIGINSLHEIGHT;//原始长
    private int ORIGINLEFTMARGIN;//原始左偏移
    private int ORIGINTOPMARGIN;//原始上偏移
    private boolean isCanMove=false;//是否移动了

    public WordBackView(Context context) {
        super(context);
        ini();
    }

    public WordBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini();

    }

//    public WordBackView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        ini();
//
//    }

    private void ini(){

        this.setOnTouchListener(mOntouchListener);
        this.setOnLongClickListener(mOnLongClickListener);
        minMove= ViewConfiguration.get(getContext()).getScaledTouchSlop();


    }



    /**
     * 初次获取控件的中心坐标
     */
    public void initCenterCordinate(int w,int h) {

//        int[] location = new int[2];
//        this.getLocationOnScreen(location);
//
//        centerX = location[0];
//        centerY = location[1];
//
//        centerX+=(this.getWidth()/2);
//        centerY+=(this.getHeight()/2);

        centerX+=(this.getLeft()+w/2);
        centerY+=(this.getTop()+h/2);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (isFirstLoad){
            calcSideLength(w,h);
            initCenterCordinate(w,h);
            ORIGINSLWIDTH=w;
            ORIGINSLHEIGHT=h;
            ORIGINLEFTMARGIN=this.getLeft();
            ORIGINTOPMARGIN=this.getTop();
            isFirstLoad=false;
        }

    }



    //计算标准尺寸参照长度
    private void calcSideLength(int w,int h){
        double diX=(w/2);
        double diY=(h/2);
        SIDELENGTH=(float)Math.sqrt(diX*diX+diY*diY);
    }

   public int getMWidth(){
       return ORIGINSLWIDTH;
   }

    public int getMHeight(){
        return ORIGINSLHEIGHT;
    }

    public int getMLeftMargin(){
        return ORIGINLEFTMARGIN;
    }

    public int getMTopMargin(){
        return ORIGINTOPMARGIN;
    }

    private OnTouchListener mOntouchListener=new OnTouchListener() {
        boolean isAtPointOfCanMove=true;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int mAction=event.getAction();

            switch (mAction){
                case MotionEvent.ACTION_MOVE:

                    //如果长按事件以发生，则可以发生移动行为
                    if (isEditAble){

                         /*此代码的主要作用是：在长按事件刚发生后的那一时刻，获取手指
                         的当前触摸点。防止由于激活长按事件过程中，手指已滑动了一段距						离，导致控件瞬间滑动一大段。当获取后isAtPointOfCanMove置为						false。避免之后的滑动一直对滑动的起始坐标重新赋值。
                           */
                        if (isAtPointOfCanMove){
                            oldX=event.getRawX();
                            oldY=event.getRawY();
                            isAtPointOfCanMove=false;
                        }

                        //获取相对滑动起始位置的XY轴上的偏移
                        transX=(event.getRawX()-oldX);
                        transY=(event.getRawY()-oldY);
                        //如果滑动的距离大于最小滑动距离，则判定为可以进行滑动
                        if (Math.sqrt(transX*transX+transY*transY)>minMove) {

                            modifyLocation();
                            isCancelClickEnable=true;
                            isCanMove=true;
                            //通知父布局不要截断触屏事件的传播
                            requestParentDisallowInterceptTouchEvent(true);

                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    isCancelClickEnable=false;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    //如果控件发生了滑动，则更新当前控件的相对原始位置的XY轴上的偏移
                    //isCanMove的作用是防止，点击，无效滑动导致的相对偏移的异常更新
                   if(isCanMove) {
                        mCurX += transX;
                        mCurY += transY;
                        //更新中心点
                        centerX += transX;
                        centerY += transY;
                        //更新友view的最终位置
                        friendView.setLocation(transX, transY, true);
                        //控件位置发生改变的回调
                        if (mOnChanedListener != null) {
                            mOnChanedListener.onChanedListner();
                        }
                       //通知父布局可以恢复截断事件传播状态
                       requestParentDisallowInterceptTouchEvent(false);
                   }
                    //不管如何将一次滑动过程过后的所有标志复位
                    isEditAble = false;
                    isCanMove=false;
                    isAtPointOfCanMove=true;
                    v.setBackgroundResource(R.drawable.edge_numal);
                    break;

            }

            return false;
        }
    };



    private OnLongClickListener mOnLongClickListener=new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            v.setBackgroundResource(R.drawable.edge_select);
            isCancelClickEnable=true;
            isEditAble=true;
            return false;
        }
    };


    /**
     * 改变
     */
    public void changeBackGroundLineColor(){
        GradientDrawable mLoDrawable=(GradientDrawable)this.getBackground();
        if(strokeColor==edgeColorSelect){
            mLoDrawable.setStroke(1,strokeColor);
        }
    }


    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);

        }
    }

    private void modifyLocation(){


        float x=transX+mCurX;
        float y=transY+mCurY;
        ViewHelper.setTranslationX(this,x);
        ViewHelper.setTranslationY(this,y);

        if(friendView==null){
            return;
        }

        friendView.setLocation(transX, transY, false);

    }


    /**
     * 设置点击事件
     * @param mOnClickListener
     */
    public void setMOnClickListner(MOnClickLisnstener mOnClickListener){
//        this.setOnClickListener(mOnClickListener);
        this.mOnClickLisnstener=mOnClickListener;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WordBackView.this.mOnClickLisnstener!=null&&!isCancelClickEnable){

                    WordBackView.this.mOnClickLisnstener.mOnClickListener();

                }
            }
        });

    }

    public void setFriendView(View v){

        this.friendView=(WordControlView)v;
    }


    /**
     *
     * @return
     */
    float[] getCenter(){



        float[] mCenter=new float[2];
        mCenter[0]=centerX;
        mCenter[1]=centerY;
        return mCenter;
    }


    /**
     *
     * @return
     */
    float getSideLength(){
        return SIDELENGTH;
    }

    public void setMRotation(float rotation){
        ViewHelper.setRotation(this,rotation);
    }


    public void setMScale(float x,float y){

        ViewHelper.setScaleX(this,x);
        ViewHelper.setScaleY(this,y);
    }

    /**
     * 设置状态改变回调监听器
     * @param onchanedListner
     */
    public void setOnchanedListner(WordViewBuilder.OnChanedListener onchanedListner){
        mOnChanedListener=onchanedListner;
    }


    //////////////////////////////////////////////////对外调整接口


    /**
     * 获取中心点在X轴上的位置
     * @return
     */
    public float getOffSetX(){
        return centerX;

    }

    /**
     * 获取中心点在Y轴上的位置
     * @return
     */
    public float getOffSetY(){

        return centerY;
    }


    /**
     * 初始化中心点位置
     * @param mCurX
     * @param mCurY
     */
    public void initData(float mCurX,float mCurY){

        this.mCurX=mCurX;
        this.mCurY=mCurY;
        centerX+=mCurX;
        centerY+=mCurY;

    }

    //单击事件回调接口
    public interface MOnClickLisnstener{
        public void mOnClickListener();
    }

//    /**
//     * 对外回调接口
//     */
//    public interface OnChanedListener{
//        public void onChangedListner();
//    }


}
