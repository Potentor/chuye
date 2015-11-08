package com.hulian.firstpage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.adapter.ChoosePicsGridViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseMorePicActivity extends Activity implements AdapterView.OnItemClickListener {
    GridView gridView;
    String pics;
    int width;
    int height;

    ChoosePicsGridViewAdapter adapter;
    List<String> ps;
    Intent intent;
    TextView finish;
    public static final int ADD_PIC = 1;
    public static final int CHANGE_PIC = 2;
    public static final int FINISH = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_more_pic);
        init();
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.grid_choose_more_pics);
        finish = (TextView) findViewById(R.id.tv_choose_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChoose();
                finish();
            }
        });
        intent = getIntent();
        pics = intent.getStringExtra("pics");
        width = intent.getIntExtra("width", 320);
        height = intent.getIntExtra("height",504);
        Log.d("width", width + "");
        String [] pic = pics.split(";");
        ps = new ArrayList<>();
        for (String p:pic){
            ps.add(p);
        }
        adapter = new ChoosePicsGridViewAdapter(this, ps);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        Log.d("ps,length", ps.size() + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_more_pic, menu);
        return true;
    }

    public void finishChoose(){
        pics = "";
        for (String pic : ps){
            pics += pic +";";
        }
        pics = pics.substring(0,pics.length()-1);
        intent.putExtra("pics",pics);
        Log.d("pics",pics);
        setResult(FINISH, intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finishChoose();
        finish();
        return true;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent mIntent = new Intent(this, ClipImageActivity.class);
        if (position < ps.size()) {
            mIntent.putExtra("width", width);
            mIntent.putExtra("height", height);
            mIntent.putExtra("position", position);
            startActivityForResult(mIntent, CHANGE_PIC);
        } else {
            mIntent.putExtra("width", width);
            mIntent.putExtra("height", height);
            startActivityForResult(mIntent, ADD_PIC);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_PIC && resultCode != ClipImageActivity.CLIP_FAIL) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                ps.set(position, data.getData().toString());
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == ADD_PIC && resultCode != ClipImageActivity.CLIP_FAIL) {
            ps.add(data.getData().toString());
            adapter.notifyDataSetChanged();
        }
    }
}
