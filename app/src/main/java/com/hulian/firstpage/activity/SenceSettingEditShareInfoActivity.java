package com.hulian.firstpage.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SimpleSenceInfo;

/**
 * Created by Administrator on 2015/3/31.
 */
public class SenceSettingEditShareInfoActivity extends ActionBarActivity {
    TextView senceTitle;
    TextView senceContent;
    String title;
    String content;

    SimpleSenceInfo senceInfo;
    public static final int EDIT_TITLE = 1;
    public static final int EDIT_CONTENT = 2;
    public static final int SETTING_BACK = 3;
    private Intent intent;

    private TextView finish;
    private TextView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence_stting_share);
        senceTitle = (TextView) findViewById(R.id.tv_setting_share_info_title);
        senceContent = (TextView) findViewById(R.id.tv_setting_share_info_content);
        intent = getIntent();
        String id = intent.getStringExtra("id");
        final DBManager dbManager = DBManager.getDBManager(this);
        senceInfo = dbManager.querySenceById(id);
        finish = (TextView) findViewById(R.id.scene_setting_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.updateSimpleSence(senceInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        back = (TextView) findViewById(R.id.tv_setting_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.updateSimpleSence(senceInfo);
                finish();
            }
        });
        senceTitle.setText(senceInfo.getTitle());
        senceContent.setText(senceInfo.getDescription());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClickEditTitle(View view) {

        Intent intent = new Intent(this, SenceSettingEditShareTitleActivity.class);
        intent.putExtra("id", senceInfo.getId());
        intent.putExtra("type", EDIT_TITLE);
        startActivityForResult(intent, EDIT_TITLE, null);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onClickEditContent(View view) {
        Intent intent = new Intent(this, SenceSettingEditShareTitleActivity.class);
        intent.putExtra("id", senceInfo.getId());
        intent.putExtra("type", EDIT_CONTENT);
        startActivityForResult(intent, EDIT_CONTENT, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        if (data != null) {
            if (requestCode == EDIT_TITLE) {

                title = data.getExtras().getString("result");
                senceTitle.setText(title);

            } else if (requestCode == EDIT_CONTENT) {
                content = data.getExtras().getString("result");
                senceContent.setText(content);
            }
        }

    }

    public void onClickFinish(View view) {


    }

    public void onClickBack(View view) {
        finish();

    }
}
