package com.hulian.firstpage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hulian.firstpage.R;
import com.hulian.firstpage.activity.SenceEditActivity;
import com.hulian.firstpage.activity.ShowSenceActivity;
import com.hulian.firstpage.adapter.SenceAdapter;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.hulian.firstpage.util.HttpClientUtils;
import com.hulian.firstpage.util.ParserUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SenceFragment extends Fragment implements OnItemClickListener {

    private PullToRefreshListView listview;
    private MessageHandler handler;
    private static final int MSG_REFRESH_COMPLETE = 1;
    private static final int MSG_LOAD_COMPLETE = 2;
    private ProgressDialog dialog;// 对话框
    private ParserUtils parser;
    /**
     * 保存已经存储到数据库的数据
     */
    static List<SimpleSenceInfo> senceDate = new ArrayList<>();
    /**
     * 保存从服务器返回经过json解析出来的数据
     */
    SenceAdapter mAdapter;

    DBManager dbManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        handler = new MessageHandler(this);
        dbManager = DBManager.getDBManager(this.getActivity());
        return init(inflater, container);
    }

    private LinearLayout init(LayoutInflater inflater, ViewGroup container) {

        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.fragment_sence, container, false);
        listview = (PullToRefreshListView) layout
                .findViewById(R.id.mypage_pull);

//        List<SimpleSenceInfo> mData = new ArrayList<>();

        mAdapter = new SenceAdapter(senceDate, getActivity(), listview);

        listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                senceDate = dbManager.querySence();
                handler.sendEmptyMessage(MSG_REFRESH_COMPLETE);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                senceDate = dbManager.querySence();
                handler.sendEmptyMessage(MSG_REFRESH_COMPLETE);

            }
        });

        listview.setOnItemClickListener(this);
        listview.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载");
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel("释放加载更多");
        listview.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新");
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel("释放刷新");

        listview.setAdapter(mAdapter);
        loadDataFromDB();
        return layout;
    }

    public void loadDataFromDB() {
        dialog = ProgressDialog.show(this.getActivity(), null, "加载中，请稍候。。。");

        new Thread(new Runnable() {

            @Override
            public void run() {
                senceDate = dbManager.querySence();
                handler.sendEmptyMessage(SenceFragment.MSG_LOAD_COMPLETE);
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


        Intent intent =null;
        if (!senceDate.get(position-1).isUpload()){
            Toast.makeText(getActivity(), "此场景未上传，进入编辑页面", Toast.LENGTH_SHORT).show();
          intent = new Intent(getActivity(), SenceEditActivity.class);
        }else {
          intent = new Intent(getActivity(), ShowSenceActivity.class);
        }

        intent.putExtra("senceId", senceDate.get(position-1).getId());
        startActivity(intent);

    }

    private List<SimpleSenceInfo> loadData() {

        List<SimpleSenceInfo> newData = new ArrayList<SimpleSenceInfo>();
        String path = CommonUtils.SENCE_LIST_PATH;

        try {
            parser = new ParserUtils(getActivity());
            // 发送请求，得到返回数据
            String httpResponse = HttpClientUtils.httpGet(path);

            Log.d("SceneFragment", httpResponse);
            JSONObject object = new JSONObject(httpResponse);
            if (object.getString("msg").equals("success")) {
                newData = parser.parserSimpleSenceInfos(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newData;
    }

    private static class MessageHandler extends Handler {
        WeakReference<SenceFragment> host;

        private MessageHandler(SenceFragment host) {
            this.host = new WeakReference<>(host);
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            if (msg.what == MSG_REFRESH_COMPLETE) {
                SenceFragment sf = host.get();
                if (sf != null) {
                    sf.createGetDataTask().execute();
                }
            }


            if (msg.what == MSG_LOAD_COMPLETE) {
                SenceFragment sf = host.get();
                if (sf != null) {
                    sf.mAdapter.setData(sf.senceDate);
                    sf.mAdapter.notifyDataSetChanged();
                    sf.listview.onRefreshComplete();
                    sf.dialog.dismiss();
                }
            }
        }
    }

    public void addSence(SimpleSenceInfo sence) {
        senceDate.add(sence);
        dbManager.insertSimpleSence(sence);
        mAdapter.notifyDataSetChanged();

    }

    public GetDataTask createGetDataTask() {

        return new GetDataTask();
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<SimpleSenceInfo>> {

        // 后台处理部分
        @Override
        protected List<SimpleSenceInfo> doInBackground(Void... params) {
            // Simulates a background job.
            return  loadData();
        }


        // 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        // 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override

        protected void onPostExecute(List<SimpleSenceInfo> newData) {
            // 在头部增加新添内容

            for (SimpleSenceInfo info : newData) {
                if (!senceDate.contains(info)) {
                dbManager.insertSimpleSence(info);
                senceDate.add(info);
                }
            }
            mAdapter.setData(senceDate);
            mAdapter.notifyDataSetChanged();
            listview.onRefreshComplete();
            super.onPostExecute(newData);
        }
    }
}
