package com.hulian.firstpage.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.adapter.WordColorGridViewAdapter;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.view.WordToImageView;

/**
 * Created by Administrator on 2015/4/15.
 */
public class SenceEditWordAcitivity extends ActionBarActivity implements View.OnClickListener ,AdapterView.OnItemClickListener{
    TextView tv_edit_word_cancel;
    TextView tv_edit_word_finish;
    EditText editText;
    TextView tv_hint;
    TextView tv_choose_color;
    TextView tv_choose_preview;
    WordToImageView word_image_view;
    public static final int EDIT_MAXLENGTH = 250;
    public static final int EDIT_FINISH = 101;
    public static final int EDIT_CANCEL = 102;
    Intent mIntent;
    Bitmap bitmap;
    TypedArray colors;
    HorizontalScrollView scrollView;
    GridView gridView;
    WordColorGridViewAdapter adapter;
    SimpleSenceInfo sence;
    SenceComponetInfo componet;
    int color;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_word);
        init();
        regist();
    }

    private void regist() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int inputLengh = s.toString().length();
                tv_hint.setText(EDIT_MAXLENGTH - inputLengh + "");

            }
        });

        tv_edit_word_finish.setOnClickListener(this);
        tv_edit_word_cancel.setOnClickListener(this);
        tv_choose_preview.setOnClickListener(this);
        tv_choose_color.setOnClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    private void init() {
        mIntent = getIntent();
        String id = mIntent.getStringExtra("id");
        componet = DBManager.getDBManager(this).queryComponetById(id);
        colors = getResources().obtainTypedArray(R.array.colors_arr);
        tv_edit_word_cancel = (TextView) findViewById(R.id.tv_edit_word_cancel);
        tv_edit_word_finish = (TextView) findViewById(R.id.tv_edit_word_finish);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_choose_color = (TextView) findViewById(R.id.tv_choose_color);
        tv_choose_preview = (TextView) findViewById(R.id.tv_choose_preview);
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(componet.getText_content());
        color = componet.getText_color();
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(EDIT_MAXLENGTH)});
        word_image_view = (WordToImageView) findViewById(R.id.word_image_view);
//        scrollView = (HorizontalScrollView) findViewById(R.id.hsv_color);
        word_image_view.setTextColor(color);
        gridView = (GridView) findViewById(R.id.gird_color);
        adapter = new WordColorGridViewAdapter(this);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_word_cancel:


                finish();
                break;
            case R.id.tv_edit_word_finish:
                Uri uri = word_image_view.storeImage();
                mIntent.setData(uri);
                mIntent.putExtra("width",word_image_view.getBitmapWidth());
                mIntent.putExtra("height",word_image_view.getBitmapHeight());
                mIntent.putExtra("text_content",editText.getText().toString());
                mIntent.putExtra("text_color",color);
                setResult(EDIT_FINISH, mIntent);

                finish();
                break;
            case R.id.tv_choose_color:
                showColorGrid();

                break;
            case R.id.tv_choose_preview:
                gridView.setVisibility(View.GONE);
                word_image_view.setText(editText.getText().toString().trim());
                word_image_view.generateImage();
                word_image_view.getMyBitmap();

                break;

        }
    }
    public void showColorGrid(){
        int isVis =  gridView.getVisibility();
        if (isVis == View.GONE){
            gridView.setVisibility(View.VISIBLE);
        }else{
            gridView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        color = colors.getColor(position, Color.BLACK);
        word_image_view.setTextColor(color);
        gridView.setVisibility(View.GONE);
    }
}
