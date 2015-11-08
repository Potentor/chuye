package com.hulian.firstpage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hulian.firstpage.R;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SenceHorizontalListViewAdapter extends BaseAdapter {

    private ArrayList<SencePageInfo> sencePages;
    private LayoutInflater inflate;

    public SenceHorizontalListViewAdapter(Context mContext, ArrayList<SencePageInfo> sencePages) {

        this.sencePages = sencePages;
        inflate = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sencePages.size();
    }

    @Override
    public Object getItem(int position) {
        return sencePages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_select_model, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = new HoldView(convertView);
        }
        SencePageInfo sencePage = sencePages.get(position);
        String picUrl = sencePage.getComponetInfos().get(0).getPics().split(";")[0];
        holdView.pic.setImageUrl(picUrl);
        return convertView;
    }

    private static class HoldView {
        SmartImageView pic;


        public HoldView(View view) {
            pic = (SmartImageView) view.findViewById(R.id.add_select_model_pic);


        }

    }
    public void setData( ArrayList<SencePageInfo> sencePages){
        this.sencePages = sencePages;
    }
    public void addModel(SencePageInfo sencePage) {
        this.sencePages.add(sencePage);
        this.notifyDataSetChanged();
    }
    public void addModel(int index,SencePageInfo sencePage) {
        this.sencePages.add(index,sencePage);
        this.notifyDataSetChanged();
    }
    public void subModel(int index) {
        this.sencePages.remove(index);
        this.notifyDataSetChanged();
    }


}
