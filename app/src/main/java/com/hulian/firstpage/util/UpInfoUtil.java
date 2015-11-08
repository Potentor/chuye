package com.hulian.firstpage.util;


import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by 海鸥2012 on 2015/4/24.
 */
public class UpInfoUtil {

    public static void schemeUpLoad(String info,Handler handler){

        info=replaceSpecialtyStr(info,null,null);
        new UpLoadThread(info,handler).start();

    }

    private static class UpLoadThread extends Thread {



        String actionUrl=CommonUtils.SENCE_UPLOAD;//接口地址
  //      String actionUrl="http://192.168.199.179:8080/Scenes/scene/upload";//接口地址
        String infos;//="{\"id\":\"4255C27C-A444-4D65-8FFB-B04379321B35\",\"title\":\"我的场景录\",\"cover\":\"/images/template/pic_01.jpg\",\"description\":\"\",\"music\":null,\"is_public\":true,\"is_recommond\":true,\"pages\":[{\"id\":\"4255C27C-A444-4D65-8FFB-B04379321B54\",\"index\":0,\"template_id\":2,\"components\":[{\"id\":\"4255C27C-A444-4D65-8FFB-B0437932aabb\",\"type\":0,\"z_index\":0,\"x\":0,\"y\":0,\"w\":100,\"h\":100,\"rotate\":0,\"anchor_x\":90,\"anchor_y\":10,\"pic_mode\":0,\"pic_num\":1,\"pics\":\"/images/template/pic_01.jpg\",\"text_content\":\"abc\",\"text_size\":20,\"text_color\":\"\",\"text_font\":\"\"}]}]}";

        private Handler mHandler;

        public UpLoadThread(String infos,Handler handler) {
            this.mHandler=handler;
            this.infos=infos;

        }



        @Override
        public void run() {
            super.run();
            doRun();
        }


        private void doRun()  {

            OutputStream os=null;
            try {
                URL url = new URL(actionUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* 允许Input、Output，不使用Cache */
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setConnectTimeout(5000);
          /* 设置传送的method=POST */
                con.setRequestMethod("POST");
          /* setRequestProperty */

                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type","application/json");
                con.setRequestProperty("Accept","application/json");
          /* 设置DataOutputStream */
//                DataOutputStream ds = new DataOutputStream();
//                ds.writeBytes();
               // ds.writeUTF();
                os = con.getOutputStream();
//                ds.flush();
//                ds.close();
                byte[] buffer = infos.getBytes("utf-8");
                os.write(buffer);
                os.flush();
                os.close();

                Log.i("tag",infos);

                byte[] buffer1  = new byte[1024];
                InputStream inputStream =(InputStream)con.getContent();

                int len =0;
                StringBuilder builder = new StringBuilder();
                while ((len=inputStream.read(buffer1))>0){
                    builder.append(new String(buffer1,0,len));

                }
                inputStream.close();

                Log.i("tag","content:"+builder);

                if (buffer==null||buffer.equals("")){

                    mHandler.sendEmptyMessage(408);
                    return;
                }else{


                    try {

                        JSONObject mJsonResultConten=new JSONObject(builder.toString());
                        int resultCode=(int)mJsonResultConten.get("code");
                        if (resultCode==0){
                            mHandler.sendEmptyMessage(200);
                        }else{
                            mHandler.sendEmptyMessage(408);
                        }


                    } catch (JSONException e) {
                        mHandler.sendEmptyMessage(408);
                        e.printStackTrace();
                    }

                }



            } catch (MalformedURLException e) {
                mHandler.sendEmptyMessage(408);
                e.printStackTrace();
            } catch (ProtocolException e) {
                mHandler.sendEmptyMessage(408);
                e.printStackTrace();
            } catch (IOException e) {
                mHandler.sendEmptyMessage(408);
                e.printStackTrace();
            }finally {
                if (os!=null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public static boolean isBlankOrNull(String str){
        if(null==str)return true;
        //return str.length()==0?true:false;
        return str.length()==0;
    }
    public static String replaceSpecialtyStr(String str,String pattern,String replace){
        if(isBlankOrNull(pattern))
            pattern="\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        if(isBlankOrNull(replace))
            replace="";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }


}
