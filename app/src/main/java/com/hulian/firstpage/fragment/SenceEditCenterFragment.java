package com.hulian.firstpage.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.adapter.SenceHorizontalListViewAdapter;
import com.hulian.firstpage.adapter.SenceJazzViewPagerAdapter;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.hulian.firstpage.view.HorizontalListView;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SenceEditCenterFragment extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<SencePageInfo> sencePages;
    LinearLayout ll_zoom;
    JazzyViewPager viewPager;
    HorizontalListView listView;
    SimpleSenceInfo sence;
    SenceJazzViewPagerAdapter viewPagerAdapter;
    SenceHorizontalListViewAdapter listViewAdapter;
    LayoutInflater inflater;
    //表示是否显示缩略图 以便确定jaz的宽和高
   public int w;
   public int h;
    boolean isShowZoom = false;

    public SenceEditCenterFragment() {
        this.sencePages = new ArrayList<SencePageInfo>();
//        inflater = LayoutInflater.from(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getActivity());
        View root = inflater.inflate(R.layout.sence_include_center, container, false);
        listView = (HorizontalListView) root.findViewById(R.id.hlv_zoom);
        ll_zoom = (LinearLayout) root.findViewById(R.id.ll_zoom);
        viewPager = (JazzyViewPager) root.findViewById(R.id.jvp_sence);
        init();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,h);
        params.addRule(RelativeLayout.BELOW,R.id.ll_zoom);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        viewPager.setLayoutParams(params);

        viewPagerAdapter = new SenceJazzViewPagerAdapter(getActivity(), sencePages, viewPager,w,h);

        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);

//        this.vp_edit_content.setPageMargin(CommonUtils.dip2px(getApplicationContext(), 18.0F));
//        this.vp_edit_content.setOffscreenPageLimit(2);
        viewPager.setOffscreenPageLimit(2);
        listViewAdapter = new SenceHorizontalListViewAdapter(getActivity(), sencePages);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setClipChildren(false);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);

//        Intent i = new Intent();
//        i.setType(“image  /*”);
//i.setAction(Intent.ACTION_GET_CONTENT);
//startActivityForResult(i, 11);
        return root;
    }

    private void init() {
        if (isShowZoom) {

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);;
            int widthPixels= dm.widthPixels;
            int heightPixels= dm.heightPixels;
            float density = dm.density;
//            int screenWidth = (int) (widthPixels * density);
            int screenHeight = heightPixels;
            Log.d("SenceEditCenterFragment",heightPixels+"");
            Log.d("SenceEditCenterFragment",density+"");
            Log.d("SenceEditCenterFragment",screenHeight+"");
            h = screenHeight - CommonUtils.dp2px(getActivity(),260);
            w = (int) (h / 504.0 * 320);

        } else {
//            h = getActivity().getWindowManager().getDefaultDisplay().getHeight() - CommonUtils.dp2px(getActivity(),135);
//            w = (int) (h / 504.0 * 320);


            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);;
            int widthPixels= dm.widthPixels;
            int heightPixels= dm.heightPixels;
            float density = dm.density;
//            int screenWidth = (int) (widthPixels * density);

            int screenHeight =heightPixels ;
            Log.d("SenceEditCenterFragment",heightPixels+"");
            Log.d("SenceEditCenterFragment",density+"");
            Log.d("SenceEditCenterFragment",screenHeight+"");
            h = screenHeight - CommonUtils.dp2px(getActivity(),160);
            w = (int) (h / 504.0 * 320);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * 设置是否显示缩略图
     */
    public void setShowZoom() {
        if (ll_zoom.getVisibility() == View.VISIBLE) {

            ll_zoom.setVisibility(View.GONE);
            isShowZoom = false;
        } else {
            ll_zoom.setVisibility(View.VISIBLE);
            isShowZoom = true;
        }
        init();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,h);
        params.addRule(RelativeLayout.BELOW, R.id.ll_zoom);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        viewPager.setLayoutParams(params);
        viewPagerAdapter.setSize(w,h);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public void initSence(SimpleSenceInfo sence) {
        this.sence = sence;
        this.sencePages = this.sence.getPages();
        viewPagerAdapter.setData(sencePages);
        listViewAdapter.setData(sencePages);
        viewPagerAdapter.notifyDataSetChanged();
        listViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d("onSaveInstanceState","onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    /**
     * 增加一页模版
     */
    public void addModel(ModelInfo info) {
        SencePageInfo sencePage = SencePageInfo.createPage(info);
        sencePage.setIndex(sencePages.size());
        this.sencePages.add(sencePage);

        update();
    }

    public void addModel(int index, ModelInfo info) {
        SencePageInfo sencePage = SencePageInfo.createPage(info);
        this.sencePages.add(index, sencePage);
        viewPagerAdapter.setData(sencePages);
        listViewAdapter.setData(sencePages);
        viewPagerAdapter.notifyDataSetChanged();
        listViewAdapter.notifyDataSetChanged();
    }

    public void subModel(int index) {
        this.sencePages.remove(index);
        viewPagerAdapter.setData(sencePages);
        listViewAdapter.setData(sencePages);
        viewPagerAdapter.notifyDataSetChanged();
        listViewAdapter.notifyDataSetChanged();
    }

    public void update() {
        viewPagerAdapter.setData(sencePages);
        listViewAdapter.setData(sencePages);
        viewPagerAdapter.notifyDataSetChanged();
        listViewAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(sencePages.size());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = LayoutInflater.from(getActivity());
        View dialog = inflater1.inflate(R.layout.sence_eidt_dialog, null);
        TextView front = (TextView) dialog.findViewById(R.id.tv_dialog_front);
        TextView behind = (TextView) dialog.findViewById(R.id.tv_dialog_behind);
        TextView delete = (TextView) dialog.findViewById(R.id.tv_dialog_delete);
        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Toast.makeText(getActivity(), "已经是第一页", Toast.LENGTH_SHORT).show();
                } else {
                    SencePageInfo sencePage = sencePages.get(position);
                    sencePages.set(position, sencePages.get(position - 1));
                    sencePages.set(position - 1, sencePage);
                    update();
                }
            }
        });
        behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == sencePages.size() - 1) {
                    Toast.makeText(getActivity(), "已经是最后一页", Toast.LENGTH_SHORT).show();
                } else {
                    SencePageInfo sencePage = sencePages.get(position);
                    sencePages.set(position, sencePages.get(position + 1));
                    sencePages.set(position + 1, sencePage);
                    update();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subModel(position);
            }
        });

        builder.setView(dialog);
        builder.show();

    }


}
