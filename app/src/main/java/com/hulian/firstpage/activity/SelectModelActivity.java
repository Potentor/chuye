package com.hulian.firstpage.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.hulian.firstpage.R;
import com.hulian.firstpage.adapter.SelectModelAdapter;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.hulian.firstpage.util.HttpClientUtils;
import com.hulian.firstpage.util.ParserUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SelectModelActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private PullToRefreshGridView select_model_PTRGridView;
    private ArrayList<ModelInfo> modelList;
    private ArrayList<ModelInfo> oldModelList;
    private ProgressDialog dialog;// 对话框
    private ParserUtils parser;
    private MessageHandler handler;
    private SelectModelAdapter adapter;
    private DBManager DBManager;
    private static final int MSG_REFRESH_COMPLETE = 1;
    /**增加模版成功*/
    public static final int ADD_MODEL_OK = 1;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        modelList = new ArrayList<ModelInfo>();
        oldModelList = new ArrayList<ModelInfo>();
        handler = new MessageHandler(this);
        select_model_PTRGridView = (PullToRefreshGridView) findViewById(R.id.add_select_model_PTRGridView);
        adapter = new SelectModelAdapter(this, modelList);
        DBManager = DBManager.getDBManager(this);
        select_model_PTRGridView.setAdapter(adapter);
        select_model_PTRGridView.setMode(PullToRefreshBase.Mode.BOTH);
        select_model_PTRGridView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        select_model_PTRGridView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        select_model_PTRGridView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放加载更多");
        select_model_PTRGridView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        select_model_PTRGridView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        select_model_PTRGridView.getLoadingLayoutProxy(true, false).setReleaseLabel("释放刷新");
        select_model_PTRGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                loadData();

            }
        });
        select_model_PTRGridView.setOnItemClickListener(this);
        loadData();
        intent = getIntent();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();

        intent.putExtra("modelId",modelList.get(position).getId());
        setResult(ADD_MODEL_OK,intent);
        finish();
    }

    /**
     * 异步加载数据方法
     */
    private static class MessageHandler extends Handler {
        WeakReference<SelectModelActivity> host;

        private MessageHandler(SelectModelActivity host) {
            this.host = new WeakReference<>(host);
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            SelectModelActivity ampa = host.get();
            if (ampa != null) {
                if (msg.what == 100) {
                    ampa.adapter.setDate(ampa.modelList);
                    ampa.adapter.upDate();

                }


                if (msg.what == 200) {
                    Toast.makeText(ampa, "网络连接错误", Toast.LENGTH_SHORT).show();

                }
                if (msg.what == 300) {
                    Toast.makeText(ampa, "数据解析错误", Toast.LENGTH_SHORT).show();

                }
                if (msg.what == 400) {
                    Toast.makeText(ampa, "未知错误", Toast.LENGTH_SHORT).show();

                }


                if (msg.what == MSG_REFRESH_COMPLETE) {
                    ampa.select_model_PTRGridView.onRefreshComplete();

                }
                ampa.dialog.dismiss();
            }
        }

    }

    private void loadData() {
        dialog = ProgressDialog.show(this, null, "加载中，请稍候。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String path = "http://192.168.199.199:8080/www/template.json";

                try {
                    parser = new ParserUtils(SelectModelActivity.this);
                    // 发送请求，得到返回数据
                    String httpResponse = HttpClientUtils.httpGet(CommonUtils.TEMPLATE_LIST_PATH);
//                    LogUtil.d("请求返回的数据", httpResponse);
//                    // 将返回的数据解析
//                    Log.d("AddMyPageActivity", httpResponse);
                    JSONObject object = new JSONObject(httpResponse);
                    if (object.getString("msg").equals("success")) {
                        modelList = parser.parserModelInfo(object);
                        handler.sendEmptyMessage(100);
                        oldModelList = DBManager.queryModel();
                        for (ModelInfo model : modelList) {
                            if (!oldModelList.contains(model)) {
                                DBManager.insertModel(model);
                            }
                        }
                    } else {
                        handler.sendEmptyMessage(200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(200);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(300);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(400);
                }finally {
                    handler.sendEmptyMessage(MSG_REFRESH_COMPLETE);
                }
            }
        }).start();
    }

}
