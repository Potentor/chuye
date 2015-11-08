package com.hulian.firstpage.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hulian.firstpage.R;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.SimpleSenceInfo;
import com.hulian.firstpage.util.HttpClientUtils;

import java.util.List;

public class SenceAdapter extends BaseAdapter implements OnClickListener {

	private PopupWindow pwShare;// 分享到朋友圈悬浮框
    private PopupWindow pwEdit;// 点击more显示选择悬浮框
    private List<SimpleSenceInfo> data;
    private Context context;
    private LayoutInflater inflater;
    private PullToRefreshListView listView;
    private int nowPosition;
    ProgressDialog mProgressDialog;
    private float offestY;

	public SenceAdapter(List<SimpleSenceInfo> data, Context context,
                        PullToRefreshListView mListView) {
		init(context, data, mListView);
	}

	private void init(Context context, List<SimpleSenceInfo> data,
                      PullToRefreshListView listView) {

		this.listView = listView;
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);

		Button bt_cancel, bt_share, bt_delete;
		ImageView iv_share_friend, iv_share_friends, iv_share_more;

		RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(
				context).inflate(R.layout.pw_share, null);
		bt_cancel = (Button) relativeLayout.findViewById(R.id.share_cancle);
		iv_share_friend = (ImageView) relativeLayout
				.findViewById(R.id.share_to_friend);
		iv_share_friends = (ImageView) relativeLayout
				.findViewById(R.id.share_to_friends);
		iv_share_more = (ImageView) relativeLayout
				.findViewById(R.id.share_to_more);

		bt_cancel.setOnClickListener(this);
		iv_share_friend.setOnClickListener(this);
		iv_share_friends.setOnClickListener(this);
		iv_share_more.setOnClickListener(this);

		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.pw_sence_menu, null);
		bt_share = (Button) linearLayout.findViewById(R.id.item_share);
		bt_delete = (Button) linearLayout.findViewById(R.id.item_delete);
		bt_share.setOnClickListener(this);
		bt_delete.setOnClickListener(this);

		pwEdit = new PopupWindow(linearLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		pwEdit.setBackgroundDrawable(new BitmapDrawable());
		pwEdit.setOutsideTouchable(true);

		pwShare = new PopupWindow(relativeLayout,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		pwShare.setBackgroundDrawable(new BitmapDrawable());
		pwShare.setOutsideTouchable(true);
		pwShare.setAnimationStyle(R.style.share_popupAnimation);
        offestY=getRawSize(TypedValue.COMPLEX_UNIT_DIP,75);
	}

	@Override
	public int getCount() {
		return data.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		RelativeLayout layout = generateView(position);

		return layout;
	}
    public void setData(List<SimpleSenceInfo> data){
        this.data=data;
    }
	/**
	 * 生成每个item的View
	 * @param position
	 * @return
	 */
	private RelativeLayout generateView(final int position) {

		final RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.item_sence, null);

		TextView tv_access = (TextView) layout
				.findViewById(R.id.main_content_item_access);
		TextView tv_share = (TextView) layout
				.findViewById(R.id.main_content_item_share);
		TextView tv_time = (TextView) layout
				.findViewById(R.id.main_content_item_date);
		TextView tv_name = (TextView) layout
				.findViewById(R.id.main_content_item_title);
		final ImageView iv_more = (ImageView) layout
				.findViewById(R.id.main_content_item_doit);

		iv_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                if (position==0) {
                    pwEdit.showAsDropDown(iv_more, -30,-(int)(offestY));
                }else{
                    pwEdit.showAsDropDown(iv_more, -30,-(int)(offestY*1.5));
                }

                nowPosition=position;
			}
		});

		tv_access.setText("查看： " + data.get(position).getViewCount());
		tv_share.setText("分享： " + data.get(position).getShareCount());
		tv_name.setText(data.get(position).getTitle());
        tv_time.setText(data.get(position).getUploadTime());
		return layout;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.share_cancle:
			pwShare.dismiss();
			break;
		case R.id.share_to_friend:
			shareFriend();
			break;
		case R.id.share_to_friends:
			shareFriends();
			break;
		case R.id.share_to_more:
			shareMore();
			break;
		case R.id.item_share:
			share();
			break;
		case R.id.item_delete:
			deleteItem();
			break;
		}

	}

	// 分享给微信好友
	private void shareFriend() {
		Toast.makeText(context, "好友", Toast.LENGTH_SHORT).show();
	}

	// 分享到朋友圈
	private void shareFriends() {
		Toast.makeText(context, "朋友圈", Toast.LENGTH_SHORT).show();
	}

	// 系统分享
	private void shareMore() {
	//	Toast.makeText(context, "更多", 500).show();

		pwShare.dismiss();
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
		sendIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(sendIntent, "快速分享吧！"));
	}

	// 删除当前点击的item
	private void deleteItem() {
		pwEdit.dismiss();
        mProgressDialog=ProgressDialog.show(this.context,null,"正向服务器申请删除.....");
        SimpleSenceInfo mLoInfo=data.get(this.nowPosition);


        //删除服务器对应场景信息
            HttpClientUtils.delSence(mLoInfo.getId(),mHandler);


	}


    private void deleteFromNativeDatabse(){

        SimpleSenceInfo mLoInfo=data.get(this.nowPosition);
        //删除本地数据库对应场景信息
        DBManager mLoDbmanager=DBManager.getDBManager(this.context);
        mLoDbmanager.deleteSimpleSence(mLoInfo);
        data.remove(this.nowPosition);
        this.notifyDataSetChanged();

        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
    }

	// 显示分享悬浮框
	private void share() {
		pwEdit.dismiss();
		pwShare.showAtLocation(listView, Gravity.BOTTOM, 0, 0);

	}


    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if (msg.what==200){
                deleteFromNativeDatabse();
            }else {

                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
            }

            if (mProgressDialog!=null){
                mProgressDialog.dismiss();
            }


        }
    };

    public float getRawSize(int unit, float size) {
            Resources r;

            r = Resources.getSystem();

        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }



}
