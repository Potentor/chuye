package com.hulian.firstpage.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulian.firstpage.R;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SenceEditTitleFragment extends Fragment {
    onTitleClickListener listener;
    TextView sence_back_tv;
    ImageView iv_select_zoom;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sence_include_titlebar, container, false);
        sence_back_tv = (TextView) root.findViewById(R.id.sence_back_tv);
        iv_select_zoom = (ImageView) root.findViewById(R.id.iv_select_zoom);
        sence_back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickBack();
            }
        });
        iv_select_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickShow();
            }
        });
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onTitleClickListener) {
            this.listener = (onTitleClickListener) activity;
        }
    }

    public interface onTitleClickListener {
        /**
         * 点击了返回按钮
         */
        void onClickBack();

        /**
         * 点击了显示缩略图按钮
         */
        void onClickShow();
    }
}
