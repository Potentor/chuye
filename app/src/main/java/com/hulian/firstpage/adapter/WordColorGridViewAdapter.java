package com.hulian.firstpage.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.util.CommonUtils;

/**
 * Created by Administrator on 2015/4/16.
 */
public class WordColorGridViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    TypedArray typedArray;

    public WordColorGridViewAdapter(Context context) {
        this.context = context;
        typedArray = context.getResources().obtainTypedArray(R.array.colors_arr);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        Log.d("typedArray", typedArray.length() + "");
        return typedArray.length();

    }

    @Override
    public Object getItem(int position) {
        return typedArray.getDrawable(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_color_grid, null);
        } else {
            view = convertView;
        }
        ImageView image = (ImageView) view.findViewById(R.id.color);

        image.setBackground(typedArray.getDrawable(position));
        return view;
    }
}
