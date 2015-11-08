package com.hulian.firstpage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.fragment.SenceFragment;
import com.hulian.firstpage.view.SlidingMenuView;

public class MainActivity extends ActionBarActivity {
    private SenceFragment sceneFragment;
    private SlidingMenuView slidingMenu;
    public static final int ADD_MODEL_OK = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slidingMenu = (SlidingMenuView) findViewById(R.id.activity_main_slidingmenu);

        if (savedInstanceState == null) {
            sceneFragment = new SenceFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, sceneFragment)
                    .commit();
        }
    }

    //显示菜单
    public void showMenu(View v) {
        slidingMenu.toggle();
    }

    //增加模板
    public void addMode(View v) {
        startActivityForResult(new Intent(this, SelectModelActivity.class), ADD_MODEL_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_MODEL_OK) {
            if (data != null) {
                data.setClass(this, SenceEditActivity.class);
                int id =  data.getIntExtra("modelId",1);
               ModelInfo model = DBManager.getDBManager(this).queryModelByModelId(id);
                final SimpleSenceInfo senceInfo = SimpleSenceInfo.createSence(model);
                Intent intent = new Intent(this, SenceEditActivity.class);
                intent.putExtra("senceId", senceInfo.getId());
                sceneFragment.addSence(senceInfo);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sceneFragment.loadDataFromDB();

    }
}