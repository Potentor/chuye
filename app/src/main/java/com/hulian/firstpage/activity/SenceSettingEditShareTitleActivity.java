package com.hulian.firstpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SimpleSenceInfo;

/**
 * Created by Administrator on 2015/3/31.
 */
public class SenceSettingEditShareTitleActivity extends ActionBarActivity {
    private Intent intent;
    private TextView titleBar;
    private EditText input;
    private TextView hint;
    private static final int EDIT_MAXLENGTH = 250;
    private static final String EDIT_TITLE = "编辑初页标题";
    private static final String EDIT_CONTENT = "编辑初页描述";
    private TextView finish;
    private TextView back;
    private SimpleSenceInfo senceInfo;
    private DBManager dbManager;
    private int type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sence_title);
        titleBar = (TextView) findViewById(R.id.tv_edit_share_titlebar);
        input = (EditText) findViewById(R.id.editText);
        hint = (TextView) findViewById(R.id.tv_hint);
        intent = getIntent();
        String id = intent.getStringExtra("id");
        dbManager = DBManager.getDBManager(this);
        senceInfo = dbManager.querySenceById(id);
        //确认该面编辑的是哪个内容
        //编辑title有字数限制  编辑content没有字数限制
        type = intent.getIntExtra("type", 1);
        if (type == SenceSettingEditShareInfoActivity.EDIT_TITLE) {
            titleBar.setText(EDIT_TITLE);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(EDIT_MAXLENGTH)});
            input.setText(senceInfo.getTitle());
            setEditChangedListener();
        } else {
            titleBar.setText(EDIT_CONTENT);
            hint.setVisibility(View.GONE);
            input.setText(senceInfo.getDescription());
        }
        finish = (TextView) findViewById(R.id.scene_setting_finish);
        back = (TextView) findViewById(R.id.tv_setting_back);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                setResult(RESULT_OK);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                intent.putExtra("result", input.getText().toString().trim());
                setResult(SenceSettingEditShareInfoActivity.SETTING_BACK, intent);
                finish();
            }
        });
    }

    private void save() {
        if (type == SenceSettingEditShareInfoActivity.EDIT_TITLE) {
            senceInfo.setTitle(input.getText().toString().trim());

        } else {
            senceInfo.setDescription(input.getText().toString().trim());
        }
        dbManager.updateSimpleSenceSimpleInfo(senceInfo);
    }

    private void setEditChangedListener() {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputLengh = s.toString().length();
                hint.setText(EDIT_MAXLENGTH - inputLengh + "");

            }
        });
    }


}
