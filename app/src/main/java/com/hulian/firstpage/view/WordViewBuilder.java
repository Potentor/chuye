package com.hulian.firstpage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hulian.firstpage.R;
import com.nineoldandroids.view.ViewHelper;


public class WordViewBuilder {


    /**
     * 初始化位置时，
     * 为了避免控件的高度宽度太大，而超出了父布局的高宽，导致控件不能正常被画出，造成控件畸形，
     * 也为了避免控件在用户放大时候由于左偏移或右偏移太大，同样造成以上问题。
     * 所以在此定义一个起始偏移，使得以上问题不会发生
     */
    private  int AJUSTLEFTMARGIN;
    private  int AJUSTTOPMARGIN;


    private  RelativeLayout mRelativeLayout;
  //  private WordControlView.OnChanedListener mControlViewOnChanedListener;
    private OnChanedListener mOnChanedListener;

    private Context mContext;

    private  float degree=0;       //旋转角度
    //private float scale=1;
    private  int  back_w=200;      //主图标宽度
    private  int back_h=200;       //主图标高度
    private  int control_w=60;    //控制图标宽度
    private  int control_h=60;    //控制图标高度
    private  int offsetX=0;      //X轴方向偏移
    private  int offsetY=0;      //Y轴方向偏移
    private  Drawable mBackDrawable;     //主图标图片
    private  Drawable mControlDrawable;  //控制图标图片

    WordBackView mWordBackView;
    WordControlView mWordControlView;

    private boolean isNeedRotate=false;  //是否需要旋转
    private boolean hasSetNetUrl=false;//是否设置了网络图片的URL
    private String url;

    public WordViewBuilder(RelativeLayout mRelativeLayout, Context context){


        this.mRelativeLayout=mRelativeLayout;
        this.mContext=context;
        WindowManager vm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        AJUSTLEFTMARGIN=vm.getDefaultDisplay().getWidth()*3;
        AJUSTTOPMARGIN=vm.getDefaultDisplay().getHeight()*3;

    }



    /**
     * 设置字体图片
     */
    public WordViewBuilder setBackImageView(Drawable drawable){

       mBackDrawable=drawable;

        return this;

    }

    /**
     * 根据ID设置字体图片
     * @param rsID
     * @return
     */
    public WordViewBuilder setBackImageView(int rsID){

        this.mBackDrawable=mContext.getResources().getDrawable(rsID);
        return this;
    }

    /**
     * 使用Bitmap设置字体图片
     * @param bitmap
     * @return
     */
    public WordViewBuilder setBackImageView(Bitmap bitmap){

        this.mBackDrawable=new BitmapDrawable(bitmap);
        return this;
    }

    /**
     * 设置控制图标
     * @param drawable
     * @return
     */
    public WordViewBuilder setControlImageView(Drawable drawable){

       this.mControlDrawable=drawable;
        return this;
    }

    /**
     * 根据ID设置控制图标
     * @param rsID
     * @return
     */
    public WordViewBuilder setControlImageView(int rsID){

        this.mControlDrawable=mContext.getResources().getDrawable(rsID);
        return this;
    }

    /**
     * 使用Bitmap设置控制图标
     * @param bitmap
     * @return
     */
    public WordViewBuilder setControlImageView(Bitmap bitmap){

        this.mControlDrawable=new BitmapDrawable(bitmap);
        return this;
    }

    /**
     * 设置控件的位置
     */
    public  WordViewBuilder setOffSet(int x,int y){

      this.offsetX=x;
      this.offsetY=y;

      return this;
    }


//    /**
//     * 设置缩放的比例
//     * @param
//     * @return  构建器对象
//     */
//    public WordViewBuilder setScale(float s){
//        this.scale=s;
//
//        return this;
//    }


    /**
     * 旋转角度
     * @param degree
     * @return
     */

    public  WordViewBuilder setRotation(float degree){

        //this.degree=(degree%360);
        this.degree=(float)(degree/Math.PI*180);
        isNeedRotate=true;
        return this;
    }

    /**
     * 设置字体图片的大小
     * @param width
     * @param heigh
     * @return
     */
    public WordViewBuilder setBackSize(int width,int heigh){

        this.back_w=width;
        this.back_h=heigh;
        return  this;
    }

    /**
     * 设置图片加载的Url
     * @param url
     * @return
     */
    public WordViewBuilder setImageUrl(String url){

        this.url=url;
        hasSetNetUrl=true;
        return this;
    }


    /**
     * 设置控制图标的大小
     * @param width
     * @param heigh
     * @return
     */
    public WordViewBuilder setControlSize(int width,int heigh){

        this.control_h=heigh;
        this.control_w=width;
        return this;
    }

//    public WordViewBuilder setOnChanedListener1(WordControlView.OnChanedListener onChanedListener){
//        this.mControlViewOnChanedListener=onChanedListener;
//        return this;
//    }

    /**
     * 设置控件状态改变回调事件
     * @param onChanedListener
     * @return
     */
    public WordViewBuilder setOnChanedListener(OnChanedListener onChanedListener){
        this.mOnChanedListener=onChanedListener;
        return this;
    }

    /**
     * 设置点击事件监听器
     * @param onClickListner
     * @return
     */
    public WordViewBuilder setOnClickListner(WordBackView.MOnClickLisnstener onClickListner){

        if(mWordBackView!=null) {
           // mWordBackView.setOnClickListener(onClickListner);
            mWordBackView.setMOnClickListner(onClickListner);
        }
        return this;
    }

//    /**
//     * 重新更新控件
//     * @return
//     */
//    public WordViewBuilder reset(){
//
//         WordViewBuilder mBulder=new WordViewBuilder(mRelativeLayout,mContext)
//               // .setBackImageView(word.get)
//                //.setControlImageView(R.drawable.btn_resize)
//                .setOffSet(this.getOffSetX(), this.getOffSetY())
//                .setBackSize(this.getMWidth(), this.getMHeight())
//                .setRotation(this.getMRotation())
//                .setImageUrl(this.getImageURL())
//                .createView();
//
//            if(mWordBackView!=null){
//                mRelativeLayout.removeView(mWordBackView);
//                mWordBackView=null;
//            }
//
//            if(mWordControlView!=null){
//                mRelativeLayout.removeView(mWordControlView);
//                mWordControlView=null;
//            }
//
//        return mBulder;
//
//    }


    /**
     * 生成相应的控件
     */
    public WordViewBuilder createView(){


        mWordBackView=new WordBackView(mContext);
        mWordControlView=new WordControlView(mContext);

        mWordBackView.setFriendView(mWordControlView);
        mWordControlView.setFriendView(mWordBackView);

        mWordControlView.setScaleType(ImageView.ScaleType.FIT_XY);
        mWordBackView.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout.LayoutParams mParams_back=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams mParams_control=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

        mParams_back.width=this.back_w;
        mParams_back.height=this.back_h;

        mParams_control.width=this.control_w;
        mParams_control.height=this.control_h;



        int ajustOffsetX=-AJUSTLEFTMARGIN;
        int ajustOffsetY=-AJUSTTOPMARGIN;



//        mParams_back.leftMargin=this.offsetX;
//        mParams_back.topMargin=this.offsetY;

        mParams_back.leftMargin=ajustOffsetX;
        mParams_back.topMargin=ajustOffsetY;



        //修改旋转角度
        if(isNeedRotate){


//            ViewHelper.setPivotX(mWordControlView,control_w/2-back_w/2);
//            ViewHelper.setPivotY(mWordControlView,control_h/2+back_h/2);
//            ViewHelper.setRotation(mWordControlView,degree);


//            mParams_control.leftMargin=(offsetX+back_w-control_w/2);
//            mParams_control.topMargin=(offsetY-control_h/2);


            double[] control_off=calc();
//            mParams_control.leftMargin=(offsetX+back_w/2-control_w/2+(int)control_off[0]);
//            mParams_control.topMargin=(offsetY+back_h/2-control_h/2+(int)control_off[1]);

            mParams_control.leftMargin=(ajustOffsetX+back_w/2-control_w/2+(int)control_off[0]);
            mParams_control.topMargin=(ajustOffsetY+back_h/2-control_h/2+(int)control_off[1]);





        }else{

//            mParams_control.leftMargin=(offsetX+back_w-control_w/2);
//            mParams_control.topMargin=(offsetY-control_h/2);

            mParams_control.leftMargin=(ajustOffsetX+back_w-control_w/2);
            mParams_control.topMargin=(ajustOffsetY-control_h/2);


        }


        mWordBackView.setBackgroundResource(R.drawable.edge_numal);

        if(!hasSetNetUrl) {
            //加载字体图片
            if (mBackDrawable == null) {

                mWordBackView.setImageResource(R.drawable.tipsview);
            } else {
                mWordBackView.setImageDrawable(mBackDrawable);
            }
        }else{
            mWordBackView.setImageUrl(url);
        }


        //控制图标加载图片
        if(mControlDrawable==null){

            mWordControlView.setImageResource(R.drawable.btn_resize);
        }else{

            mWordControlView.setImageDrawable(mControlDrawable);
        }



        mRelativeLayout.addView(mWordBackView,mParams_back);
        mRelativeLayout.addView(mWordControlView,mParams_control);


        mWordControlView.setFriendRotate(degree);
        mWordBackView.setMRotation(degree);

//        ViewHelper.setTranslationX(mWordControlView,back_w+offsetX);
//        ViewHelper.setTranslationY(mWordControlView,back_h+offsetY);
//        ViewHelper.setTranslationX(mWordBackView,back_w+offsetX);
//        ViewHelper.setTranslationY(mWordBackView,back_h+offsetY);

        ViewHelper.setTranslationX(mWordControlView,AJUSTLEFTMARGIN+offsetX);
        ViewHelper.setTranslationY(mWordControlView,AJUSTTOPMARGIN+offsetY);
        ViewHelper.setTranslationX(mWordBackView,AJUSTLEFTMARGIN+offsetX);
        ViewHelper.setTranslationY(mWordBackView,AJUSTTOPMARGIN+offsetY);

        mWordBackView.initData(AJUSTLEFTMARGIN+offsetX,AJUSTTOPMARGIN+offsetY);
        mWordControlView.initData(AJUSTLEFTMARGIN+offsetX,AJUSTTOPMARGIN+offsetY);

        mWordControlView.setOnchanedListner(new OnChanedListener() {
            @Override
            public void onChanedListner() {
                if (mOnChanedListener!=null) {
                    mOnChanedListener.onChanedListner();
                }
            }
        });

        mWordBackView.setOnchanedListner(new OnChanedListener(){

            @Override
            public void onChanedListner() {
                if(mOnChanedListener!=null) {
                    mOnChanedListener.onChanedListner();
                }
            }
        });




        //mWordBackView.setMScale(scale,scale);


        return this;

    }


    /**
     * 计算控制图标的位置
     * @return
     */
    private double[] calc(){

        double[] cor=new double[2];


        double rightAngleSide1=(back_h/2);
        double rightAngleSide2=(back_w/2);
        double slope=(rightAngleSide1/rightAngleSide2);
        double temp_degree=Math.atan(slope);//参考角斜率
        double cor_degree=(temp_degree/Math.PI*180);//参考角（角度）
        double side=Math.sqrt(rightAngleSide1*rightAngleSide1+rightAngleSide2*rightAngleSide2);//等腰三角形腰长


        if(degree<=cor_degree){


            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
            double angle = (temp_degree-slope_degree);//直角一角的斜率（用其算XY轴上的偏移）
            cor[0] = (side * Math.cos(angle));
            cor[1] =  (0-side * Math.sin(angle));




        }else if(degree<=(90+cor_degree)){


            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
            double angle=(slope_degree-temp_degree);
            cor[0]=(side*Math.cos(angle));
            cor[1]=(side*Math.sin(angle));


        }else if(degree<=(180+cor_degree)){


            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
            double angle=(slope_degree-Math.PI/2-temp_degree);
            cor[0]=(0-side*Math.sin(angle));
            cor[1]=(side*Math.cos(angle));


        }else if(degree<=(270+cor_degree)){


            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
            double angle=(slope_degree-Math.PI-temp_degree);
            cor[0]=(0-side*Math.cos(angle));
            cor[1]=(0-side*Math.sin(angle));

        }else{

            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
            double angle = (slope_degree-Math.PI-Math.PI/2-temp_degree);//直角一角的斜率（用其算XY轴上的偏移）
            cor[0] = (side * Math.sin(angle));
            cor[1] =  (0-side * Math.cos(angle));

        }

        return cor;

    }


    /**
     * 获取图片的URL
     * @return
     */
    public String getImageURL(){
        return url;
    }

//
//    private WordViewBuilder getMe(){
//        return this;
//    }

    /**
     * 获取旋转角度
     * @return
     */
    public float getMRotation(){
        return mWordControlView.getMRotation();

    }

    /**
     * 获取X轴上偏移
     * @return
     */
    public int getOffSetX(){

        return (int)(mWordBackView.getOffSetX()-back_w*mWordControlView.getmScale()/2);
        //return (int)(mWordBackView.getOffSetX()-back_w/2);
    }

    /**
     * 获取Y轴上偏移
     * @return
     */
    public int getOffSetY(){

        return (int)(mWordBackView.getOffSetY()-back_h*mWordControlView.getmScale()/2);
        //return (int)(mWordBackView.getOffSetY()-back_h/2);
    }

    /**
     * 获取最终宽度
     * @return
     */
    public int getMWidth(){

        return (int)(mWordControlView.getmScale()*back_w);
       // return (int)(back_w);
    }

    /**
     * 获取最终高度
     * @return
     */
    public int getMHeight(){

        Log.i("log","scale"+mWordControlView.getmScale());

        return (int)(mWordControlView.getmScale()*back_h);
//        return (int)(back_h);
    }

    public float getMScale(){

        return (float)mWordControlView.getmScale();
    }


    public interface OnChanedListener{

        public  void onChanedListner();
    }


//    private double[] calc(){
//
//        double[] cor=new double[2];
//
//       // float calc_degree=degree;
//       // degree=(360-degree);
//
//        float slope=((back_h/2)/(back_w/2));
//        double temp_degree=Math.atan(slope);//斜率
//
//        double cor_degree=(temp_degree/Math.PI*180*2);//参考角（角度）
//
//        double side=Math.sqrt(Math.pow(back_w/2,2)+Math.pow(back_h/2,2));//等腰三角形腰长
//        double ab;//直角三角形斜边（此直角三角形的两直角边分别是X轴上的偏移与Y轴上的偏移）
//
//       // double fixedAngle=(cor_degree/2/180*Math.PI);//固定角斜率..不随旋转角度改变而改变
//        double fixedAngle=temp_degree;//固定角斜率..不随旋转角度改变而改变
//
//        //double slope_degree=(degree/180*Math.PI);//旋转角的斜率
//
//            if(degree<=cor_degree){
//
//
//            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
//            double bigAngle = ((180 - degree) / 2 / 180 * Math.PI);//等腰三角形一角斜率
//            double angle = (Math.PI - bigAngle - fixedAngle);//直角一角的斜率（用其算XY轴上的偏移）
//
//            ab = (side / Math.sin(bigAngle) * Math.sin(slope_degree));//计算直角斜边长
//
//            cor[0] = (ab * Math.cos(angle));
//            cor[1] =  (ab * Math.sin(angle));
//
//
//
//
////            double bigAngle=((180-degree)/2/180*Math.PI);//等腰三角形一角斜率
////            double angle=(bigAngle-fixedAngle);
////
////            Log.i("degree","side"+side);
////            Log.i("degree","bigAngle"+bigAngle/Math.PI*180);
////            Log.i("degree","slope_degree"+slope_degree/Math.PI*180);
////            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
////
////            cor[0]=(0-ab*Math.cos(angle));
////            cor[1]=(ab*Math.cos(angle));
////            Log.i("degree","ab"+ab);
//
//
//        }else if(degree<=180){
//
////            double bigAngle=((180-degree)/2/180*Math.PI);//等腰三角形一角斜率
////            double angle=(fixedAngle-bigAngle);
////            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
////
////            cor[0]=(0-ab*Math.cos(angle));
////            cor[1]=(0-ab*Math.cos(angle));
//            double bigAngle=((180-degree)/2/180*Math.PI);//等腰三角形一角斜率
//            double angle=(bigAngle+fixedAngle);
//            double slope_degree=(degree/180*Math.PI);//旋转角的斜率
//            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
//
//            cor[0]=(0-ab*Math.cos(angle));
//            cor[1]=(ab*Math.sin(angle));
//
//
//        }else if(degree<=(180+cor_degree)){
//
////            double bigAngle=((360-degree)/2/180*Math.PI);//等腰三角形一角斜率
////            double angle=(bigAngle+fixedAngle);
////            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
////
////            cor[0]=(0-ab*Math.cos(angle));
////            cor[1]=(0-ab*Math.cos(angle));
//
//            double bigAngle=((degree-180)/2/180*Math.PI);//等腰三角形一角斜率
//            double angle=(fixedAngle-bigAngle);
//            double slope_degree=((360-degree)/180*Math.PI);//旋转角的斜率
//            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
//
//            cor[0]=(0-ab*Math.cos(angle));
//            cor[1]=(ab*Math.sin(angle));
//
//
//        }else {
//
////            double bigAngle = ((360 - degree) / 2 / 180 * Math.PI);//等腰三角形一角斜率
////            double angle = (Math.PI - bigAngle - fixedAngle);
////            ab = (side / Math.sin(bigAngle) * Math.sin(slope_degree));
////
////            cor[0] = (0 - ab * Math.cos(angle));
////            cor[1] = (0 - ab * Math.cos(angle));
//            double bigAngle=((degree-180)/2/180*Math.PI);//等腰三角形一角斜率
//            double angle=(bigAngle-fixedAngle);
//            double slope_degree=((360-degree)/180*Math.PI);//旋转角的斜率
//            Log.i("degree","side"+side);
//            Log.i("degree","bigAngle"+bigAngle/Math.PI*180);
//            Log.i("degree","slope_degree"+slope_degree/Math.PI*180);
//            ab=(side/Math.sin(bigAngle)*Math.sin(slope_degree));
//
//            cor[0]=(0-ab*Math.cos(angle));
//            cor[1]=(0-ab*Math.sin(angle));
//            Log.i("degree","ab"+ab);
//
//        }
//
//        //degree=calc_degree;
//        return cor;
//
//    }




}
