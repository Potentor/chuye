package com.hulian.firstpage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hulian.firstpage.R;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/14.
 */
public class ChoosePicsGridViewAdapter extends BaseAdapter {
    Context mContext;
    List<String> pics;
    LayoutInflater inflater;

    public ChoosePicsGridViewAdapter(Context context, List<String> pics) {
        this.mContext = context;
        this.pics = pics;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return pics.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;

//        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_choose_pics, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
//        } else {
//            holdView = new HoldView(convertView);
//        }
        if (position < pics.size()) {
            holdView.imageView.setImageUrl(pics.get(position));
        }else {
            holdView.imageView.setImageResource(R.drawable.add_btn_normal);
        }
        return convertView;
    }

    public static class HoldView {
        SmartImageView imageView;

        public HoldView(View view) {
            this.imageView = (SmartImageView) view.findViewById(R.id.item_siv_choosepics);

        }
    }
}
