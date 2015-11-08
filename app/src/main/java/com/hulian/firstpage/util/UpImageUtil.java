package com.hulian.firstpage.util;


import android.os.Handler;
import android.util.Log;

import com.upyun.block.api.exception.UpYunException;
import com.upyun.block.api.listener.CompleteListener;
import com.upyun.block.api.main.UploaderManager;
import com.upyun.block.api.utils.UpYunUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Created by 海鸥2012 on 2015/4/24.
 */
public class UpImageUtil {



    public static UpLoadThread schemeUpLoad(String fileName,String filePath,Handler handler){

            UpLoadThread mUpLoadThread=new UpLoadThread(fileName,filePath,handler);
            mUpLoadThread.start();
            return mUpLoadThread;

    }

    public static class UpLoadThread extends Thread {


        // 空间名
        private String bucket = "chuye";
        // 表单密钥
        String formApiSecret = "PlZT81XnxijjqXM+HqO/lWzzzB4=";

        String savePath = "/";
        String filePath;
        private Handler mHandler;

        public UpLoadThread(String fileName,String filePath,Handler handler) {
            this.savePath += fileName;
            this.mHandler=handler;
            this.filePath=filePath;


        }


        @Override
        public void run() {
            super.run();
            doRun();
        }


        private void doRun() {

            try {

                Log.e("hel","result:");

                CompleteListener completeListener = new CompleteListener() {
                    @Override
                    public void result(boolean isComplete, String result, String error) {
                        try {
                            Log.e("hel","result:"+result);

                            if (mHandler!=null) {

                                if (result==null){
                                    mHandler.sendEmptyMessage(408);
                                    return;
                                }

                                JSONObject mJson = new JSONObject(result);
                                String code=mJson.get("code").toString();
                                Log.e("hel","error:"+error);
                                Log.e("hel","isComplete:"+isComplete);
                                Log.e("hel","result:"+result);

                                if (code.equals("200")){
                                    mHandler.sendEmptyMessage(200);
                                    return;
                                }

                                mHandler.sendEmptyMessage(408);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (mHandler!=null) {
                                mHandler.sendEmptyMessage(408);
                            }
                        }
                    }
                };


                File mFile = new File(filePath);
                UploaderManager uploaderManager = UploaderManager.getInstance(bucket);
                uploaderManager.setConnectTimeout(30000);
                uploaderManager.setResponseTimeout(60);

                Map<String, Object> paramsMap = uploaderManager.fetchFileInfoDictionaryWith(mFile, savePath);
                String policyForInitial = UpYunUtils.getPolicy(paramsMap);
                String signatureForInitial = UpYunUtils.getSignature(paramsMap, formApiSecret);
               // uploaderManager.setConnectTimeout(30000);
                uploaderManager.upload(policyForInitial, signatureForInitial, mFile, null, completeListener);


            } catch (UpYunException e) {
                e.printStackTrace();
                if (mHandler!=null) {
                    mHandler.sendEmptyMessage(408);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (mHandler!=null) {
                    mHandler.sendEmptyMessage(408);
                }
            }

        }
    }


}
