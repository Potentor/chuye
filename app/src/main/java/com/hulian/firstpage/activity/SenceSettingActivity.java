package com.hulian.firstpage.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SimpleSenceInfo;

public class SenceSettingActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    SimpleSenceInfo senceInfo;
    private TextView textTitle;
    private TextView textContent;
    private CheckBox checkBoxPrivate;
    private CheckBox checkBoxPublic;
    private TextView tv_setting_finish;
    String title;
    String content;
    boolean is_public;
    boolean is_recommond;
    DBManager db;
    private static final int EDIT_SHARE_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence_setting);
        Intent intent = getIntent();
        String senceId = intent.getStringExtra("senceId");
        senceInfo = DBManager.getDBManager(this).querySenceById(senceId);
        textTitle = (TextView) findViewById(R.id.scene_share_title);
        textContent = (TextView) findViewById(R.id.scene_share_content);
        checkBoxPrivate = (CheckBox) findViewById(R.id.scene_share_private);
        tv_setting_finish = (TextView) findViewById(R.id.tv_setting_finish);
        db = DBManager.getDBManager(this);
        tv_setting_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.updateSimpleSence(senceInfo);
                finish();
            }
        });
        checkBoxPrivate.setOnCheckedChangeListener(this);
        checkBoxPublic = (CheckBox) findViewById(R.id.scene_share_public);
        checkBoxPublic.setOnCheckedChangeListener(this);
        textTitle.setText(senceInfo.getTitle());
        textContent.setText(senceInfo.getDescription());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void ClickInfo(View view) {

        Intent intent = new Intent();
        intent.setClass(this, SenceSettingEditShareInfoActivity.class);
        intent.putExtra("id", senceInfo.getId());


        startActivityForResult(intent, EDIT_SHARE_INFO, null);

    }

    public void ClickPublic(View view) {
        if (checkBoxPublic.isChecked()) {
            checkBoxPublic.setChecked(false);
        } else {
            checkBoxPublic.setChecked(true);
            if (checkBoxPrivate.isChecked()) {
                checkBoxPrivate.setChecked(false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_SHARE_INFO) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }

    }

    public void ClickPrivate(View view) {
        if (checkBoxPrivate.isChecked()) {
            checkBoxPrivate.setChecked(false);
        } else {
            checkBoxPrivate.setChecked(true);
            if (checkBoxPublic.isChecked()) {
                checkBoxPublic.setChecked(false);

            }
        }
    }

    public void onClickBGM(View view) {
        Intent intent = new Intent(this, SenceSettingBGMActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.scene_share_private:
                senceInfo.setRecommond(checkBoxPrivate.isChecked());
                break;
            case R.id.scene_share_public:
                senceInfo.setPublic(checkBoxPublic.isChecked());
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleSenceInfo sence = db.querySenceById(senceInfo.getId());
        senceInfo.setDescription(sence.getDescription());
        senceInfo.setTitle(sence.getTitle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
