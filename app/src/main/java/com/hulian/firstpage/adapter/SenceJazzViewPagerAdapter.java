package com.hulian.firstpage.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hulian.firstpage.R;
import com.hulian.firstpage.activity.ChooseMorePicActivity;
import com.hulian.firstpage.activity.ClipImageActivity;
import com.hulian.firstpage.activity.SenceEditActivity;
import com.hulian.firstpage.activity.SenceEditWordAcitivity;
import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.Anchor;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.hulian.firstpage.view.WordBackView;
import com.hulian.firstpage.view.WordViewBuilder;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.OutlineContainer;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/24.
 */
public class SenceJazzViewPagerAdapter extends PagerAdapter {
    private int mChildCount = 0;
    private Context mContext;
    private ArrayList<SencePageInfo> sencePages;
    private JazzyViewPager jvp_sence;
    ArrayList<Anchor> anchors = new ArrayList<>();
    int w;
    int h;


    /**
     * 用来标识被按下的是那个页面的position
     */
    private int position;

    public SenceJazzViewPagerAdapter(Context mContext, ArrayList<SencePageInfo> sencePages, JazzyViewPager jvp_sence, int jazWidth, int jazHeight) {
        this.mContext = mContext;
        this.sencePages = sencePages;
        this.jvp_sence = jvp_sence;
        w = jazWidth;
        h = jazHeight;
        Map<String, View> views = new HashMap<String, View>();
    }

//    private SenceJazzViewPagerAdapter(Context mContext, ArrayList<ModelInfo> models, JazzyViewPager jvp_sence, int position) {
//        this(mContext, models, jvp_sence);
//        this.position = position;
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(jvp_sence.findViewFromObject(position));//删除页卡
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public RelativeLayout build(SencePageInfo info, int position) {
        this.position = position;
        final RelativeLayout layout = new RelativeLayout(mContext);
//        Toast.makeText(mContext, "build", Toast.LENGTH_SHORT).show();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);

        layout.setLayoutParams(params);
        layout.measure(0, 0);
        ArrayList<SenceComponetInfo> componentInfos = info.getComponetInfos();
        Collections.sort(componentInfos);
        anchors.clear();
        for (final SenceComponetInfo componetInfo : componentInfos) {
            int offSetX = layout.getMeasuredWidth() * componetInfo.getX() / 100;
            int offSetY = layout.getMeasuredHeight() * componetInfo.getY() / 100;
            int SizeX = layout.getMeasuredWidth() * componetInfo.getW() / 100;
            int SizeY = layout.getMeasuredHeight() * componetInfo.getH() / 100;
            int Anchor_x = layout.getMeasuredWidth() * componetInfo.getAnchor_x() / 100;
            int Anchor_y = layout.getMeasuredHeight() * componetInfo.getAnchor_y() / 100;

            float rotation = componetInfo.getRotate();
            String[] pics = componetInfo.getPics().split(";");

//                        Log.d("getMeasuredWidth", layout.getMeasuredWidth() + "");
//                        Log.d("getMeasuredHeight", layout.getMeasuredHeight() + "");
//                        Log.d("offSetX", offSetX + "");
//                        Log.d("offSetY", offSetY + "");
            switch (componetInfo.getType()) {
                case "1":
                    for (int i = 0; i < pics.length; i++) {

                        final WordViewBuilder mWord1 = new WordViewBuilder(layout, mContext);
                        mWord1.setOffSet(offSetX, offSetY);
                        mWord1.setBackSize(SizeX, SizeY);
                        mWord1.setRotation(rotation);
                        mWord1.setImageUrl(pics[i]);

                        mWord1.createView();
                        mWord1.setOnChanedListener(new WordViewBuilder.OnChanedListener() {

                            @Override
                            public void onChanedListner() {

                                Log.d("mWord1.getMRotation()", mWord1.getMRotation()+"");
                                componetInfo.setRotate((float) mWord1.getMRotation());
                                componetInfo.setX((int) ((mWord1.getOffSetX() / (float) layout.getMeasuredWidth()) * 100));
                                componetInfo.setY((int) ((mWord1.getOffSetY() / (float) layout.getMeasuredHeight()) * 100));
                                componetInfo.setW((int) ((mWord1.getMWidth() / (float) layout.getMeasuredWidth()) * 100));
                                componetInfo.setH((int) ((mWord1.getMHeight() / (float) layout.getMeasuredHeight()) * 100));
                            }
                        });
                        mWord1.setOnClickListner(new WordClickListener(position, componetInfo.getId()));

                    }
                    break;
                case "0":
                    LayoutInflater inflatera = LayoutInflater.from(mContext);
                    for (int i = 0; i < pics.length; i++) {
                        SmartImageView image = new SmartImageView(mContext);
                        image.setImageUrl(pics[i]);
                        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(SizeX, SizeY);
                        params1.setMargins(offSetX, offSetY, 0, 0);
                        image.setLayoutParams(params1);
                        layout.addView(image);
                        Anchor anchor = new Anchor();
                        anchor.setAnchor_y(Anchor_y);
                        anchor.setAnchor_x(Anchor_x);
                        anchor.setComponet_w(SizeX);
                        anchor.setComponet_h(SizeY);
                        anchor.setComponet_id(componetInfo.getId());
                        anchor.setComponet_mode(componetInfo.getPic_mode());
                        anchor.setComponet_pics(componetInfo.getPics());
                        anchor.setComponet_Zindex(componetInfo.getZ_index());
                        anchors.add(anchor);

                    }
                    break;

            }
            for (Anchor anchor : anchors) {
                MyOnClickListener myOnClickListener = new MyOnClickListener(anchor.getComponet_mode(), anchor.getComponet_w(), anchor.getComponet_h(), anchor.getComponet_id(), anchor.getComponet_pics());
                SencePageInfo page = sencePages.get(position);
                ModelInfo model = DBManager.getDBManager(mContext).queryModelByModelId(page.getTemplate_id());

                if (anchor.getComponet_Zindex() == 1  && model.getCode().equals(CommonUtils.CODE_MODEL_SCRATCH_CARD)) {//证明是涂抹的上层遮罩层
                    TextView textView = new TextView(mContext);
                    textView.setBackgroundResource(R.color.Crimson);//猩红色背景
                    textView.setText("换图（涂抹）");
                    textView.setTextColor(Color.WHITE);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(anchor.getAnchor_x(), anchor.getAnchor_y(), 0, 0);
                    textView.setOnClickListener(myOnClickListener);
                    layout.addView(textView, params1);
                } else {

                    ImageView imageView = new ImageView(mContext);
                    imageView.setImageResource(R.drawable.btn_changepic);
                    RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(CommonUtils.dp2px(mContext, 30), CommonUtils.dp2px(mContext, 30));
                    params1.setMargins(anchor.getAnchor_x(), anchor.getAnchor_y(), 0, 0);
                    imageView.setOnClickListener(myOnClickListener);
                    layout.addView(imageView, params1);
                }
            }


        }
        return layout;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout view = build(sencePages.get(position), position);
        container.addView(view);
        jvp_sence.setObjectForPosition(view, position);

        return view;
    }

    @Override
    public int getCount() {
        return sencePages.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == object;
        } else {
            return view == object;
        }
    }

    public void addPage(SencePageInfo sencePages) {
        this.sencePages.add(sencePages);
        this.notifyDataSetChanged();
    }

    public void addPage(int index, SencePageInfo sencePages) {
        this.sencePages.add(index, sencePages);
        this.notifyDataSetChanged();
    }

    public void subPage(int index) {
        this.sencePages.remove(index);
        this.notifyDataSetChanged();
    }

    public void setData(ArrayList<SencePageInfo> sencePages) {
        this.sencePages = sencePages;
    }

    public void setSize(int w, int h) {
        this.w = w;
        this.h = h;

    }

    @Override

    public void notifyDataSetChanged() {

        mChildCount = getCount();

        super.notifyDataSetChanged();

    }


    @Override

    public int getItemPosition(Object object) {

        if (mChildCount > 0) {

            mChildCount--;

            return POSITION_NONE;

        }

        return super.getItemPosition(object);

    }

    private class WordClickListener implements WordBackView.MOnClickLisnstener {
        int position;//用来确定是哪一页上面的零件被改变
        String id;//用来确定是哪个零件被改变

        private WordClickListener(int position, String id) {
            this.position = position;
            this.id = id;
        }


        @Override
        public void mOnClickListener() {
            Intent intent = new Intent(mContext, SenceEditWordAcitivity.class);
            intent.putExtra("position", position);
            intent.putExtra("id", id);

            ((SenceEditActivity) mContext).startActivityForResult(intent, SenceEditActivity.CHANGE_WORD);
        }
    }


    private class MyOnClickListener implements View.OnClickListener {
        String mode;//用确定是选择多张图片还是选择一张图片
        int width;//用来确定截取图片的宽度
        int height;//用来确定截取图片的高度
        String id;//用来确定是哪个零件被改变
        String pics;

        private MyOnClickListener(String mode, int width, int height, String id, String pics) {
            this.mode = mode;
            this.width = width;
            this.height = height;
            this.id = id;
            this.pics = pics;
        }

        @Override
        public void onClick(View v) {
            if (mode.equals("0")) {//被按下的下表为单数  进单张图片选择器
                Intent intent = new Intent(mContext, ClipImageActivity.class);
                intent.putExtra("width", width);
                intent.putExtra("height", height);
                intent.putExtra("position", position);
                intent.putExtra("id", id);

                ((SenceEditActivity) mContext).startActivityForResult(intent, SenceEditActivity.CLIP_ONE_PIC);

            } else { //被按下的下表为偶数 进多张图片选择器
                Toast.makeText(mContext, " 多张图片选择器", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ChooseMorePicActivity.class);

                intent.putExtra("width", width);
                intent.putExtra("height", height);
                intent.putExtra("position", position);
                intent.putExtra("id", id);
                intent.putExtra("pics", pics);


                ((SenceEditActivity) mContext).startActivityForResult(intent, SenceEditActivity.CLIP_MORE_PIC);

            }
        }
    }
}
