package com.hulian.firstpage.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.hulian.firstpage.R;
import com.hulian.firstpage.adapter.SenceSettingBGMAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Administrator on 2015/4/3.
 */
public class SenceSettingBGMActivity extends ActionBarActivity {
    StickyListHeadersListView listView;

    SenceSettingBGMAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sence_setting_bgm);
        listView = (StickyListHeadersListView) findViewById(R.id.list);
        mAdapter = new SenceSettingBGMAdapter(this);
        listView.setAdapter(mAdapter);

    }
}