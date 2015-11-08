package com.hulian.firstpage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hulian.firstpage.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Administrator on 2015/4/3.
 */
public class SenceSettingBGMAdapter extends BaseAdapter implements StickyListHeadersAdapter,
        SectionIndexer {
    Context mContext;
    LayoutInflater inflater;
    String[] mMusic;
    int[] mSectionIndices;
    String[] mSectionLetters;
    long[] headId;

    public SenceSettingBGMAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        getSectionIndices();
        getSectionLetters();
        getMusic();
        getHearId();
    }

    private void getSectionIndices() {
        mSectionIndices = new int[] { 0, 5, 10, 15, 20, 25, 30, 35, 40 };
    }

    private void getSectionLetters() {
        mSectionLetters = new String[] { "中国风", "动感", "宏大", "忧伤", "轻快", "浪漫",
                "经典", "舒缓", "趣味" };

    }

    private void getMusic() {
        mMusic = new String[mSectionLetters.length * 5];
        for (int i = 0; i < mMusic.length; i++) {
            mMusic[i] = mSectionLetters[i / 5] + i % 5;
        }

    }

    private void getHearId() {
        headId = new long[mSectionLetters.length];
        for (int i = 0; i < headId.length; i++) {
            headId[i] = 1000000L + i;
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mMusic.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMusic[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_sence_setting_bgm_foot, null);
        } else {
            view = convertView;
        }
        TextView foot = (TextView) view.findViewById(R.id.foot);
        foot.setText(mMusic[position]);
        return view;
    }

    @Override
    public Object[] getSections() {

        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.item_sence_setting_bgm_head, null);
        } else {
            view = convertView;
        }
        TextView head = (TextView) view.findViewById(R.id.head);
        head.setText(mSectionLetters[position/5]);
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        // TODO Auto-generated method stub
        return headId[position / 5];
    }

    public void clear() {
        mMusic = new String[0];
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    public void restore() {
        getMusic();
        getSectionIndices();
        getSectionLetters();
        notifyDataSetChanged();
    }

}
