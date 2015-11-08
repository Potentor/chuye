package com.hulian.firstpage.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.fragment.SenceEditBottomFragment;
import com.hulian.firstpage.fragment.SenceEditCenterFragment;
import com.hulian.firstpage.fragment.SenceEditTitleFragment;
import com.hulian.firstpage.util.CommonUtils;
import com.hulian.firstpage.util.JsonUtil;
import com.hulian.firstpage.util.UpInfoUtil;

import java.lang.ref.WeakReference;


public class SenceEditActivity extends ActionBarActivity implements SenceEditTitleFragment.onTitleClickListener, SenceEditBottomFragment.onBottomClickListener {
    SimpleSenceInfo sence;
    ModelInfo model;
    SenceEditCenterFragment fragment;
    FragmentManager fragmentManager;
    public static final int ADD_REQUESTCODE = 1;
    public static final int CLIP_ONE_PIC = 1111;
    public static final int CLIP_MORE_PIC = 11;
    public static final int CHANGE_WORD = 111;
    public static final int CHANGE_INFO = 112;
    private ProgressDialog dialog;// 对话框
    private MessageHandler handler;
    DBManager db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence_edit1);
        fragmentManager = getSupportFragmentManager();
        handler = new MessageHandler(this);
        Intent intent = getIntent();
        db = DBManager.getDBManager(this);
        String senceId = intent.getStringExtra("senceId");
        sence =db.querySenceById(senceId);
        fragment = (SenceEditCenterFragment) fragmentManager.findFragmentByTag("SenceEditCenterFragment");
//        Toast.makeText(this,  sence.getPages().get(0).getComponetInfos().get(0).getPics(),Toast.LENGTH_SHORT).show();
        fragment.initSence(sence);

    }


    @Override
    public void onClickAdd() {
        Intent intent = new Intent(SenceEditActivity.this, SelectModelActivity.class);

        startActivityForResult(intent, ADD_REQUESTCODE);
    }

    @Override
    public void onClickPlay() {
        //todo
        dialog = ProgressDialog.show(this, null, "加载中，请稍候。。。");


        UpInfoUtil.schemeUpLoad(JsonUtil.getSenceJson(sence).toString(),handler);
        Log.e("json", JsonUtil.getSenceJson(sence).toString());
    }

    @Override
    public void onClickSetting() {
        Intent intent = new Intent(this, SenceSettingActivity.class);
        db.updateSimpleSence(sence);
        intent.putExtra("senceId", sence.getId());
        startActivityForResult(intent,CHANGE_INFO);
    }

    @Override
    public void onClickBack() {
        finish();
    }

    @Override
    public void onClickShow() {
        fragment.setShowZoom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUESTCODE) {
            if (data != null) {
                int modelId = data.getIntExtra("modelId", 1);
                model = db.queryModelByModelId(modelId);
                fragment = (SenceEditCenterFragment) fragmentManager.findFragmentByTag("SenceEditCenterFragment");


                fragment.addModel(model);

            }
        }
        if (requestCode == CHANGE_INFO) {
            SimpleSenceInfo sence = db.querySenceById(this.sence.getId());
            this.sence.setTitle(sence.getTitle());
            this.sence.setDescription(sence.getDescription());

        }
        if (requestCode == CLIP_ONE_PIC) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                String id = data.getStringExtra("id");
                Log.d("position", position + "");
                Log.d("id", id + "");
                Uri uri = data.getData();


                if (uri != null) {
                    for (SenceComponetInfo componet : sence.getPages().get(position).getComponetInfos()) {
                        if (componet.getId().equals(id)) {
                            componet.setPics(uri.toString());
                        }
                    }
                }
                fragment.update();
                Toast.makeText(this, "图片以收到", Toast.LENGTH_SHORT).show();
            }


        }
        if (requestCode == CLIP_MORE_PIC) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                String id = data.getStringExtra("id");
                String pics = data.getStringExtra("pics");
                Log.d("pics", pics);
                for (SenceComponetInfo componet : sence.getPages().get(position).getComponetInfos()) {
                    if (componet.getId() .equals(id) ) {
                        componet.setPics(pics);
                    }
                }
                fragment.update();
                Toast.makeText(this, "图片以收到", Toast.LENGTH_SHORT).show();
            }


        }
        if (requestCode == CHANGE_WORD) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                String id = data.getStringExtra("id");
                String text_content = data.getStringExtra("text_content");
                int text_color = data.getIntExtra("text_color", Color.BLACK);
                String pics = data.getStringExtra("pics");
                Uri uri = data.getData();
                if (uri != null) {
                    for (SenceComponetInfo componet : sence.getPages().get(position).getComponetInfos()) {
                        if (componet.getId() .equals(id) ) {
                            componet.setPics(uri.toString());
                            int width = data.getIntExtra("width", 0);
                            int height = data.getIntExtra("height", 0);
                            if (width != 0 && height != 0) {
                                width = width * 100 / fragment.w;
                                height = height * 100 / fragment.h;
                                componet.setW(width);
                                componet.setH(height);
                                componet.setText_color(text_color);
                                componet.setText_content(text_content);


                            }
                        }
                    }
                }
                fragment.update();
                Toast.makeText(this, "图片以收到", Toast.LENGTH_SHORT).show();
            }


        }
        db.updateSimpleSence(sence);
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, sence.getPages().size() + "", Toast.LENGTH_SHORT).show();
        db.updateSimpleSence(sence);
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private static class MessageHandler extends Handler {
        WeakReference<SenceEditActivity> host;

        private MessageHandler(SenceEditActivity host) {
            this.host = new WeakReference<>(host);
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            if (msg.what == 200) {
                SenceEditActivity sf = host.get();
                if (sf != null) {
                   sf.dialog.dismiss();
                   DBManager dbManager = DBManager.getDBManager(sf);
                    sf.sence.setUpload(true);
                    dbManager.updateSimpleSence(sf.sence);
                   Intent intent = new Intent(sf,ShowSenceActivity.class);
                    intent.putExtra("senceId",sf.sence.getId());
                    sf.startActivity(intent);
                }
            }
            if (msg.what == 408) {
                SenceEditActivity sf = host.get();
                if (sf != null) {
                    sf.dialog.dismiss();
                  Toast.makeText(sf,"上传失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
