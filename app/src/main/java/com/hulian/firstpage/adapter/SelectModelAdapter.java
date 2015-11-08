package com.hulian.firstpage.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.util.CommonUtils;
import com.loopj.android.image.SmartImageView;

import java.util.List;

//import com.hulian.firstpage.util.LoadImage;

/**
 * Created by Administrator on 2015/3/18.
 */
public class SelectModelAdapter extends BaseAdapter {

    protected LayoutInflater inflater;
    // 加载图片之前的默认图片
//    private Bitmap defaultBitmap;

    private SmartImageView smartImageView;

    private List<ModelInfo> modelList;
    public SelectModelAdapter(Context context, List<ModelInfo> modelList) {


        this.modelList = modelList;
        inflater = LayoutInflater.from(context);

    }



    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HoldView holdView = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_select_model, null);
            holdView = new HoldView(view);
            view.setTag(holdView);
        } else {
            holdView = new HoldView(view);
        }
        ModelInfo model = modelList.get(i);
//        holdView.pic.setImageBitmap(defaultBitmap);
        holdView.name.setText(model.getName());
        holdView.pic.setImageUrl(model.getCover());

        Log.d("AddMyPageAdapter", model.getCover());

        return view;
    }

    private static class HoldView {
        SmartImageView pic;
        TextView name;

        public HoldView(View view) {
            pic = (SmartImageView) view.findViewById(R.id.add_select_model_pic);
            name = (TextView) view.findViewById(R.id.add_select_model_name);

        }

    }

    public void setDate(List<ModelInfo> modelList) {
        this.modelList = modelList;
    }

    public void upDate() {
        this.notifyDataSetChanged();
    }

    public void clear() {
        modelList.clear();
    }

    /**
     * 根据isClearOld判断是否清除所有数据，并添加一条数据到数据集合中
     */
    public void appendData(ModelInfo t, boolean isClearOld) {
        if (t == null) { //非空验证
            return;
        }
        if (isClearOld) {
            clear();
        }
        modelList.add(t);
    }

    /**
     * 根据isClearOld判断是否清除所有数据，并添加数据集合到数据集合中
     */
    public void appendData(List<ModelInfo> data, boolean isClearOld) {
        if (data == null) { //非空验证
            return;
        }
        if (isClearOld) {
            clear();
        }
        modelList.addAll(data);
    }

    /**
     * 根据isClearOld判断是否清除所有数据，并添加一条数据到数据集合中顶部
     */
    public void appendDataTop(ModelInfo t, boolean isClearOld) {
        if (t == null) { //非空验证
            return;
        }
        if (isClearOld) {
            clear();
        }
        modelList.add(0, t);
    }

    /**
     * 根据isClearOld判断是否清除所有数据，并添加数据集合到数据集合中顶部
     */
    public void appendDataTop(List<ModelInfo> data, boolean isClearOld) {
        if (data == null) { //非空验证
            return;
        }
        if (isClearOld) {
            clear();
        }
        modelList.addAll(0, data);
    }


}
