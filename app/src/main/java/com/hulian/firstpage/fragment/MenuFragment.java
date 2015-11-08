package com.hulian.firstpage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hulian.firstpage.R;

public class MenuFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_menu, container,false);
		root.findViewById(R.id.menu_aboutUs).setOnClickListener(this);
		root.findViewById(R.id.menu_clearCashe).setOnClickListener(this);
		root.findViewById(R.id.menu_commonQuestion).setOnClickListener(this);
		root.findViewById(R.id.menu_feedBack).setOnClickListener(this);
		root.findViewById(R.id.menu_myMessage).setOnClickListener(this);
		root.findViewById(R.id.menu_quit).setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_aboutUs:
			aboutUs();
			break;
		case R.id.menu_clearCashe:
			clearCashe();
			break;
		case R.id.menu_commonQuestion:
			commonQuestion();
			break;
		case R.id.menu_feedBack:
			feedBack();
			break;
		case R.id.menu_myMessage:
			myMessage();
			break;
		case R.id.menu_quit:
			quit();
			break;
		}

	}

	// 退出
	public void quit() {
		Toast.makeText(getActivity(), "quit",Toast.LENGTH_SHORT).show();
	}

	// 常见问题
	public void commonQuestion() {
		Toast.makeText(getActivity(), "commonQuestion", Toast.LENGTH_SHORT).show();

	}

	// 清理缓存
	public void clearCashe() {
		Toast.makeText(getActivity(), "clearCashe", Toast.LENGTH_SHORT).show();

	}

	// 我的消息
	public void myMessage() {
		Toast.makeText(getActivity(), "myMessage", Toast.LENGTH_SHORT).show();

	}

	// 关于我们
	public void aboutUs() {
		Toast.makeText(getActivity(), "aboutUs", Toast.LENGTH_SHORT).show();

	}

	// 意见反馈
	public void feedBack() {
		Toast.makeText(getActivity(), "feedBack", Toast.LENGTH_SHORT).show();

	}

}
