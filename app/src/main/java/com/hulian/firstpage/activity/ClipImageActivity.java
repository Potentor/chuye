package com.hulian.firstpage.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.util.BitMapUtils;
import com.hulian.firstpage.util.UUIDUtil;
import com.hulian.firstpage.util.UpImageUtil;
import com.hulian.firstpage.view.ClipImageView;
import com.hulian.firstpage.view.MClipView;
import com.upyun.block.api.main.UploaderManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Handler;


public class ClipImageActivity extends ActionBarActivity {


    private ClipImageView mClipImageView;
    private ProgressDialog mProgressDialog;
    private int width;
    private int height;
    private RelativeLayout layout;
    private Intent mIntent;
    public static final int CLIP_FAIL=1;
    private String mFileName;
    private String myFilePath;
    private Bitmap mBitmap;
    private UpImageUtil.UpLoadThread mUpLoadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RelativeLayout layout=(RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main,null);

        setContentView(R.layout.clipeimage);
//        setContentView(layout);

        width = 450;
        height = 650;

        mIntent = getIntent();
        if (mIntent != null) {
            width = mIntent.getExtras().getInt("width", 450);
            height = mIntent.getExtras().getInt("height", 650);
        }

        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);


    }

    private void initView() {

        layout = (RelativeLayout) findViewById(R.id.selectpicture_rl_rl1);
        mClipImageView = new ClipImageView(this);
        mClipImageView.setBorderWidth(width);
        mClipImageView.setBorderHeight(height);

        MClipView mClipView = new MClipView(this);
        mClipView.setBorderWidth(width);
        mClipView.setBorderHeight(height);


        layout.addView(mClipImageView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(mClipView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public void startClip(View v) {

        mProgressDialog = ProgressDialog.show(this, null, "图片上传中。。。");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (mUpLoadThread!=null&&mUpLoadThread.isAlive()){
                    mUpLoadThread.interrupt();
                    Toast.makeText(ClipImageActivity.this, "已终止图片上传！", Toast.LENGTH_LONG).show();
                }
            }
        });
        clipAndSave();
    }


    public void cancel(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 1 || data == null) {
            Toast.makeText(this, "错误:result: " + resultCode + "request: " + requestCode + "data: " + data, Toast.LENGTH_SHORT).show();
            setResult(CLIP_FAIL,null);
            finish();
            return;
        }


        Uri localUri = data.getData();
        if (localUri == null) {
            Toast.makeText(this, "错误2", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        initImage(localUri);


    }


    /**
     * 开始剪辑图片
     */
    private void clipAndSave() {


        //检测sd卡的状态
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "sd卡异常，请检查sd卡", Toast.LENGTH_LONG).show();
            return;
        }
        //检测SD卡中是否有图片储存的路径
        //1.无建立
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        myFilePath = rootPath + "/page/imas";
        File mFile = new File(myFilePath);
        if (!mFile.exists()) {
            boolean ifSuccssful = mFile.mkdirs();
            if (!ifSuccssful) {
                Toast.makeText(this, "sd卡路径创建失败！", Toast.LENGTH_LONG).show();
                return;
            }
        }


        //生成路径中没有出现过的文件名字
        mFileName= UUIDUtil.getUUID()+".jpg";



//        String[] allFiles = mFile.list();
//        boolean isExist;
//        Random mR = new Random(System.currentTimeMillis());
//        do {
//            isExist = false;
//            int nameNum = (int) (mR.nextDouble() * 10000);
//            mFileName = String.valueOf(nameNum);
//            for (String str : allFiles) {
//                if (str.equals(mFileName)) {
//                    isExist = true;
//                    break;
//                }
//
//            }
//
//        } while (isExist);


        this.mBitmap = null;
        this.mBitmap = this.mClipImageView.clip();
        saveToSDcard();
        //上传到服务器
        File tempFile=new File(myFilePath, mFileName);
        UpImageUtil.schemeUpLoad(mFileName, tempFile.getAbsolutePath(), mHandler);

    }

    private void saveToSDcard(){
        //保存
        try {
            boolean result = BitMapUtils.saveBitmap(myFilePath, mFileName, mBitmap, Bitmap.CompressFormat.JPEG);
            if (!result) {
                Toast.makeText(this, "图片存入sd卡过程失败", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri mUri = Uri.fromFile(new File(myFilePath, mFileName));
            mIntent.setData(mUri);

            if ((this.mBitmap != null) && (!this.mBitmap.isRecycled()))
                this.mBitmap.recycle();
            System.gc();


        } catch (OutOfMemoryError localOutOfMemoryError) {

            Toast.makeText(this, "可用内存不多了，这次不能继续添加了", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
        }
        finally {
            if ((this.mBitmap != null) && (!this.mBitmap.isRecycled()))
                this.mBitmap.recycle();
            System.gc();
        }
    }


    /**
     * 初始化图片
     *
     * @param localUri
     */
    private void initImage(Uri localUri) {

        try {
            // int i = CommonUtils.getOrientation(getApplicationContext(), localUri);
            Matrix localMatrix = new Matrix();
            //  localMatrix.postRotate(70);
            BitmapFactory.Options localOptions = new BitmapFactory.Options();
            localOptions.inSampleSize = 1;
            localOptions.inJustDecodeBounds = true;
            FileDescriptor localFileDescriptor = getContentResolver().openFileDescriptor(localUri, "r").getFileDescriptor();
            BitmapFactory.decodeFileDescriptor(localFileDescriptor, null, localOptions);

            if ((!localOptions.mCancel) && (localOptions.outWidth != -1)) {
                if (localOptions.outHeight == -1)
                    return;

                localOptions.inSampleSize = computeSampleSize(localOptions, -1, this.width * this.height);


                localOptions.inJustDecodeBounds = false;
                localOptions.inDither = false;
                localOptions.inPurgeable = true;
                localOptions.inInputShareable = true;
                localOptions.inTempStorage = new byte[32768];
                Bitmap localBitmap = BitmapFactory.decodeFileDescriptor(localFileDescriptor, null, localOptions);
                //  if (i != 0)
                localBitmap = Bitmap.createBitmap(localBitmap, 0, 0, localBitmap.getWidth(), localBitmap.getHeight(), localMatrix, true);
                if (localBitmap != null) {
                    this.mClipImageView.setImageBitmap(localBitmap);
                    return;
                }
            }
        } catch (FileNotFoundException localFileNotFoundException) {
            // LogUtils.e("SelectPicActivity", localFileNotFoundException.getMessage(), localFileNotFoundException);
            Toast.makeText(getApplicationContext(), "提取图片失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } catch (OutOfMemoryError localOutOfMemoryError) {
        }

    }

    public int computeSampleSize(BitmapFactory.Options options,
                                 int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    android.os.Handler mHandler=new android.os.Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what==200){
                Toast.makeText(ClipImageActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, mIntent);
                finish();
            }

            if (msg.what==408){
                Toast.makeText(ClipImageActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();

            }
            mProgressDialog.dismiss();

        }
    };





}
