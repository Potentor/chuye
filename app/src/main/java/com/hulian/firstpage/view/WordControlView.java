package com.hulian.firstpage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hulian.firstpage.R;
import com.nineoldandroids.view.ViewHelper;


/**
 * 图片背景view
 */
public class WordControlView extends ImageView{


    private float DEFAULT_ANGLESNAP=0;
    private  float DEFAULT_TRANSLATESNAP=3;
    private double DEFAULT_SIZE=0.00f;


    private WordBackView friendView;
    private boolean isEditAble=false;
    private float oldX,oldY;//手指按下的位置
    private float transX,transY;//手指相对按下位置的偏移
    private float centerX,centerY;//backView中心点坐标
    private float mCurX=0,mCurY=0;//控制坐标当前的偏移
    private float viewX,viewY;//控制图标当前的相对父布局的中心点坐标
    private float angle_old_x,angle_old_y;
    private float angle_now_x,angle_now_y,scale_now_x,scale_now_y;
    private float mRotate=0;
    private double mScale=0;
    private double mCurScale=1;
    private float SIDELENGTH;//尺寸参考长度

    private boolean HaveGetSideLength=false;
    private boolean ControlDownStart=true;
    private boolean isChangeAble=false;


//    private OnChanedListener mOnChanedListener;
    private WordViewBuilder.OnChanedListener mOnChanedListener;


   // private RelativeLayout.LayoutParams mParams;

    public WordControlView(Context context) {
        super(context);
        ini();
    }

    public WordControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini();

    }

    public WordControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ini();

    }

    private void ini(){
        this.setOnTouchListener(mOntouchListener);
        this.setOnLongClickListener(mOnLongClickListener);
        DEFAULT_TRANSLATESNAP=ViewConfiguration.get(getContext()).getScaledTouchSlop();
        DEFAULT_SIZE=0.01f;
        DEFAULT_ANGLESNAP=1;
       // this.mParams=(RelativeLayout.LayoutParams)this.getLayoutParams();
    }



    private OnTouchListener mOntouchListener=new OnTouchListener() {

        boolean isAtPointOfCanMove=true;
        boolean  isCanMove=false;

        @Override
            public boolean onTouch(View v, MotionEvent event) {

            int mAction=event.getAction();

            switch (mAction){
                case MotionEvent.ACTION_MOVE:
                    if (isEditAble){

                        if (isAtPointOfCanMove){
                            oldX=event.getRawX();
                            oldY=event.getRawY();
                            isAtPointOfCanMove=false;
                        }

                        transX=(event.getRawX()-oldX);
                        transY=(event.getRawY()-oldY);

                        if (Math.sqrt(transX*transX+transY*transY)>DEFAULT_TRANSLATESNAP){
                            //计算手指相对手指按下位置的偏移
//                            transX = (event.getRawX() - oldX);
//                            transY = (event.getRawY() - oldY);
//                        scale_now_x=angle_now_x=event.getRawX();
//                        scale_now_y=angle_now_y=event.getRawY();


                            //计算控制坐标的相对父布局位置
                            scale_now_x = angle_now_x = (viewX + transX);
                            scale_now_y = angle_now_y = (viewY + transY);

                            modifyLocation();
                            changeRotationAndSize();
                            isCanMove=true;
                            //isChangeAble = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    ajustCenter();

                    requestParentDisallowInterceptTouchEvent(true);

                    //
                    if(ControlDownStart){
//                        angle_old_x=oldX;
//                        angle_old_y=oldY;
//                        angle_now_x=(v.getLeft()+v.getWidth()/2);
//                        angle_now_y=(v.getTop()+v.getHeight()/2);
                        angle_old_x=viewX;
                        angle_old_y=viewY;
                        ControlDownStart=false;
                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                        isEditAble = false;
                        ControlDownStart = true;
                        isAtPointOfCanMove=true;
                        friendView.setBackgroundResource(R.drawable.edge_numal);
                    if(isCanMove) {
                        mCurX += transX;
                        mCurY += transY;
                        viewX += transX;
                        viewY += transY;

//                        if (isChangeAble) {
//                            isChangeAble = false;
                            //回调
                            if (mOnChanedListener != null) {
                                mOnChanedListener.onChanedListner();
                          //  }
                                isCanMove=false;
                        }
                    }
                    break;
            }

            return false;
        }
    };




    private OnLongClickListener mOnLongClickListener=new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            friendView.setBackgroundResource(R.drawable.edge_select);
            isEditAble=true;
            return false;
        }
    };


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewX+=(this.getLeft()+w/2);
        viewY+=(this.getTop()+h/2);


    }


    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);

        }
    }


    //调整中心坐标
    private void ajustCenter(){

        float[] mCenter=friendView.getCenter();
        centerX=mCenter[0];
        centerY=mCenter[1];

        //计算尺寸的参考长度，仅计算一次，获取控件最原始的对角线一半的长度
        if(!HaveGetSideLength){

            SIDELENGTH=(float)calcDistance(viewX,viewY,centerX,centerY);
            HaveGetSideLength=true;
        }

    }

    //获取两点之间距离
    private double calcDistance(double oldX,double oldY,double curX,double curY){

        double diX=Math.abs(curX-oldX);
        double diY=Math.abs(curY-oldY);
        return Math.sqrt(diX*diX+diY*diY);
    }


    /**
     * 计算旋转方向
     * @return
     */
    private boolean calcHandWise(){
        //两向量X乘实现旋转方向的判断
        double nx=angle_now_x-centerX;
        double ny=angle_now_y-centerY;
        double ox=angle_old_x-centerX;
        double oy=angle_old_y-centerY;

        double temp=nx*oy-ny*ox;


        if (temp>0)
            return true;
        else
            return false;

    }


    /**
     * 计算旋转角度，根据三角形三边长来求一角角度原理
     */
    private void calcRotation() {


        double sideOne=calcDistance(angle_now_x,angle_now_y,centerX,centerY);
        double sideAnother=calcDistance(angle_old_x,angle_old_y,centerX,centerY);
        double sideOposite=calcDistance(angle_now_x,angle_now_y,angle_old_x,angle_old_y);

        double cos=(sideOne*sideOne+sideAnother*sideAnother-sideOposite*sideOposite)/(2*sideOne*sideAnother);
        double radian=Math.acos(cos);
        double degr=radian/Math.PI*180;
        //计算旋转的方向
        if (calcHandWise())
            degr=-degr;

        if(Math.abs(degr)>DEFAULT_ANGLESNAP){
            this.mRotate+=(float)degr;
            angle_old_x=angle_now_x;
            angle_old_y=angle_now_y;
        }
    }



    /**
     * 计算尺寸缩放
     */
    private void calcScale(){

        //计算此时手指位置到控件中心点的距离
        double sideOne=calcDistance(scale_now_x,scale_now_y,centerX,centerY);

//        if(!HaveGetSideLength){
//            SIDELENGTH=(float)calcDistance(oldX,oldY,centerX,centerY);
//            HaveGetSideLength=true;
//        }

        //计算相对控件原始尺寸的缩放比例，SIDELENGTH为控件最原始的对角	//线一半的长度
        double scale=sideOne/SIDELENGTH-1;

        if (Math.abs(scale)>DEFAULT_SIZE) {
            mScale =  scale;
        }
    }


    /**
     * 改变位置
     */
    private void modifyLocation(){




//        ViewHelper.setPivotX(this,getWidth()/2);
//        ViewHelper.setPivotY(this,getHeight()/2);
        ViewHelper.setTranslationX(this,mCurX+transX);
        ViewHelper.setTranslationY(this,mCurY+transY);

    }


    /**
     * 改变旋转角度和缩放尺寸
     */
    private void changeRotationAndSize(){

//        scale_now_x=angle_now_x=(this.getLeft()+this.getWidth()/2);
//        scale_now_y=angle_now_y=(this.getTop()+this.getHeight()/2);

//        Log.i("test3","nowX"+scale_now_x);
//        Log.i("test3","nowY"+scale_now_y);

        //计算缩放的尺寸
        calcScale();
         // friendView.setMScale((float) (mCurScale + mScale), (float) (mCurScale + mScale));
        //获取WordBackView此时的布局属性
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)friendView.getLayoutParams();
//        int mLoWidth=params.width;
//        int mLoHeight=params.height;
        int mLoWidth=friendView.getMWidth();
        int mLoHeight=friendView.getMHeight();


        params.width=(int)((mCurScale+mScale)*friendView.getMWidth());
        params.height=(int)((mCurScale+mScale)*friendView.getMHeight());

        /*此代码很重要，因为只改变控件的宽高，控件的左上角的位置不变，控件的放大是
        	以左上角的位置为缩放支点的。所以缩放后，需要调整中心点的位置，进行位置微调
        */
        params.leftMargin=friendView.getMLeftMargin()-(params.width-mLoWidth)/2;
        params.topMargin=friendView.getMTopMargin()-(params.height-mLoHeight)/2;


        friendView.setLayoutParams(params);
        //计算此时控件应该旋转的角度
        calcRotation();
        //直接用ViewHelper改变控件的旋转角度
        friendView.setMRotation(mRotate);


//
//        params.leftMargin=params.leftMargin-(params.width-mLoWidth)/2;
//        params.topMargin=params.topMargin-(params.height-mLoHeight)/2;
//
//        friendView.setLayoutParams(params);


    }


    /**
     * 友View调用的方法，以调整自己的位置
     * @param x  X方向上的偏移
     * @param y  Y方向上的偏移
     */
    public void setLocation(float x,float y,boolean isFinish){

       if (isFinish) {
           mCurX+=x;
           mCurY+=y;
           viewX+=x;
           viewY+=y;
           ViewHelper.setTranslationX(this, mCurX);
           ViewHelper.setTranslationY(this, mCurY);

       }else{

           ViewHelper.setTranslationX(this, mCurX + x);
           ViewHelper.setTranslationY(this, mCurY + y);
       }

    }


    /**
     * 设置状态改变回调监听器
     * @param onchanedListner
     */
    public void setOnchanedListner(WordViewBuilder.OnChanedListener onchanedListner){
        mOnChanedListener=onchanedListner;
    }

    public void setFriendView(View v){

        this.friendView=(WordBackView)v;

    }


    /**
     * 初始化中心点位置
     * @param mCurX
     * @param mCurY
     */
    public void initData(float mCurX,float mCurY){

        this.mCurX=mCurX;
        this.mCurY=mCurY;
        viewX+=mCurX;

        viewY+=mCurY;
    }


    /**
     * 设置旋转初始值
     * @param rotate
     */
    public void setFriendRotate(float rotate){
        this.mRotate=rotate;

    }


    /**
     * 获取旋转角度(保证在0~360之间)
     * @return
     */
    public float getMRotation(){

        float temRotation=mRotate;
        if(mRotate<0){

//            do{
//                temRotation+=360;
//
//            }while(temRotation<0);

            while(temRotation<-180){
                temRotation+=360;
            }

            return (float)(temRotation/180*Math.PI);

        }

        while(temRotation>180){
            temRotation-=360;
        }


        return (float)(temRotation/180*Math.PI);
    }


    /**
     * 获取缩放比例
     * @return
     */
    public double getmScale(){
        return mCurScale+mScale;
    }


//    /**
//     * 对外回调接口
//     */
//    public interface OnChanedListener{
//        public void onChangedListner();
//    }


}
