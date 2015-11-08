package com.hulian.firstpage.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.hulian.firstpage.util.UUIDUtil;
import com.hulian.firstpage.util.UpImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordToImageView extends ImageView
{

    private String mText="";
    private Paint mPaint;
    private  float WORD_BACED_SIZE=25;//基本字体尺寸
    private Bitmap mBitmap;
    private static final int TIMESFORDISTORTION=3;
   // private boolean isDrawBackColor=false;
   // private int backColor=0;


    public WordToImageView(Context context) {
        super(context);
        init(context);
    }

    public WordToImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WordToImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    private  void init(Context context){
        mPaint=new Paint();
//        mPaint.setDither(true);
//        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        float scale=context.getResources().getDisplayMetrics().density;
        WORD_BACED_SIZE*=scale;
        mPaint.setTextSize(WORD_BACED_SIZE*TIMESFORDISTORTION);

    }


//    public void setBackColor(int color){
//        if (mPaint==null)
//            return;
//        backColor=color;
//        isDrawBackColor=true;
//    }



    /**
     * 存储图片到sd卡
     */
    public Uri storeImage(){

        if (mBitmap==null)
            return null;


        //检测sd卡的状态
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getContext(), "sd卡异常，请检查sd卡", Toast.LENGTH_LONG).show();
            return null;
        }

        //检测SD卡中是否有图片储存的路径
        //1.无建立
        String rootPath=Environment.getExternalStorageDirectory().getAbsolutePath();
        String myFilePath=rootPath+"/page/imas";
        File mFile=new File(myFilePath);
        if(!mFile.exists()){
            boolean ifSuccssful=mFile.mkdirs();
            if(!ifSuccssful){
                return null;
            }
        }


        //生成路径中没有出现过的文件名字
        String mFileName= UUIDUtil.getUUID()+".png";
//        String[] allFiles=mFile.list();
//        boolean isExist;
//        Random mR=new Random(System.currentTimeMillis());
//        do
//        {
//            isExist=false;
//            int nameNum=(int)(mR.nextDouble()*1000);
//            mFileName=String.valueOf(nameNum);
//            for(String str:allFiles){
//                if(str.equals(mFileName))
//                {
//                    isExist=true;
//                    break;
//                }
//
//            }
//
//        }while(isExist);

        Uri mUri=null;

        //保存
        try
        {
            boolean result = saveBitmap(myFilePath, mFileName, mBitmap, Bitmap.CompressFormat.PNG);
            if (!result){
                return null;
            }

             mUri=Uri.fromFile(new File(myFilePath,mFileName));

        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {

            Toast.makeText(getContext(), "可用内存不多了，这次不能继续添加了", Toast.LENGTH_SHORT).show();
        }


        //
        File tempFile= new File(myFilePath,mFileName);
        UpImageUtil.schemeUpLoad(mFileName,tempFile.getAbsolutePath(),null);

        return mUri;

    }


    /**
     * 将剪辑的图片存入sd卡  page/imas
     * @param mFilePath
     * @param mFileName
     * @param paramBitmap
     * @param mFormat
     * @return
     */
    private  boolean saveBitmap(String mFilePath, String mFileName, final Bitmap paramBitmap, final Bitmap.CompressFormat mFormat)
    {
        FileOutputStream mFileOutPutStream=null;
        try
        {
            mFileOutPutStream=new FileOutputStream(new File(mFilePath,mFileName));
            paramBitmap.compress(mFormat, 80, mFileOutPutStream);
            mFileOutPutStream.flush();
            mFileOutPutStream.close();

        }catch (IOException mIoexception){
            try {
                if (mFileOutPutStream != null)
                    mFileOutPutStream.close();
            }catch(IOException exception2){}

            return false;
        }
        return true;

    }




    /**
     * 设置所要生成的文字
     * @param text
     */
    public void setText(String text){
        mText=text;
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public void setTextColor(int color){

        mPaint.setColor(color);
    }

    /**
     * 生成图片
     */
    public void generateImage(){


        //确定几行
        if(mText==null||mText.equals("")){
            return;
        }

        int maxWidth=0;
        int rows=0;
        int lastPosition=0;
        List<String> mListString=new ArrayList<String>();

        for(int i=0;i<mText.length();i++){

            if(mText.charAt(i)=='\n')
            {
                String str_tem=mText.substring(lastPosition,i);
                mListString.add(str_tem);
                int width_tem=getTextWidth(mPaint,str_tem);
                if(width_tem>maxWidth){
                    maxWidth=width_tem;
                }
                rows++;
                lastPosition=i+1;

            }

        }
        rows++;
        String str_temp01=mText.substring(lastPosition,mText.length());
        int width_tem=getTextWidth(mPaint,str_temp01);
        if(width_tem>maxWidth){
            maxWidth=width_tem;
        }
        
        if (maxWidth==0)
            return;
        
        mListString.add(str_temp01);
        Paint.FontMetrics metrics=mPaint.getFontMetrics();
        float eachHeight=metrics.bottom-metrics.top;
        float mHeight=rows*eachHeight;




        //回收之前生成的位图
        if (mBitmap!=null){
            mBitmap.recycle();
            System.gc();
           // mBitmap=null;
        }


//        //标准尺寸，放大五倍不失真
//        maxWidth*=TIMESFORdistortion;
//        mHeight*=TIMESFORdistortion;
//        eachHeight*=TIMESFORdistortion;


        //画出图片
        mBitmap = Bitmap.createBitmap(maxWidth+10,(int)mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);

//        //画背景
//        if (isDrawBackColor){
//            canvas.drawColor(backColor);
//        }
        float wordY=-metrics.ascent;
        for (String str:mListString){

            canvas.drawText(str, 0,wordY ,mPaint);
            wordY+=eachHeight;
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        this.setImageBitmap(mBitmap);
    }

    /**
     * 获取字符串的宽度
     * @param paint
     * @param str
     * @return
     */
    private int getTextWidth(Paint paint,String str){

        if (paint==null||str==null){
            return -1;
        }

        int len=str.length();
        if(len<=0)
            return 0;

        float[] widths=new float[str.length()];
        paint.getTextWidths(str,widths);
        int mWidth=0;
        for (int i=0;i<widths.length;i++){
            mWidth+=Math.ceil(widths[i]);
        }

        return mWidth;
    }

    /**
     * 获取生成的图片
     * @return
     */
    public Bitmap getMyBitmap(){

        if (mBitmap==null){
            return null;
        }

        return Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(),mBitmap.getHeight());

    }

    /**
     * 获取图片宽度
     * @return
     */
    public int getBitmapWidth(){
        if(mBitmap==null)
            return 0;
        return mBitmap.getWidth()/TIMESFORDISTORTION;
    }

    /**
     * 获取图片高度
     * @return
     */
    public int getBitmapHeight(){
        if(mBitmap==null)
            return 0;
        return mBitmap.getHeight()/TIMESFORDISTORTION;
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //回收资源
        if (mBitmap!=null){
            mBitmap.recycle();
            mBitmap=null;
        }
    }


}