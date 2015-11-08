package com.hulian.firstpage.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hulian.firstpage.R;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SenceEditBottomFragment extends Fragment {

    onBottomClickListener listener;

    ImageView iv_bottom_set;
    ImageView iv_bottom_play;
    ImageView iv_bottom_add;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sence_include_bottom, container, false);
        iv_bottom_set = (ImageView) root.findViewById(R.id.iv_bottom_set);
        iv_bottom_play = (ImageView) root.findViewById(R.id.iv_bottom_play);
        iv_bottom_add = (ImageView) root.findViewById(R.id.iv_bottom_add);
        iv_bottom_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSetting();
            }
        });
        iv_bottom_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickPlay();
            }
        });
        iv_bottom_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickAdd();
            }
        });
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onBottomClickListener) {
            this.listener = (onBottomClickListener) activity;
        }
    }

    public interface onBottomClickListener {
        /**
         * 点击了返回按钮
         */
        void onClickAdd();

        /**
         * 点击了显示缩略图按钮
         */
        void onClickPlay();

        void onClickSetting();
    }
}
