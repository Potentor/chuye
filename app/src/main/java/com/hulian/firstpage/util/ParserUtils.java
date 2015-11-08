package com.hulian.firstpage.util;

import android.content.Context;
import android.util.Log;

import com.hulian.firstpage.domain.ModelComponetInfo;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserUtils {

    private Context context;

    public ParserUtils(Context context) {
        this.context = context;
    }
    //解析
    //解析从服务器返回json数据

    public ArrayList<ModelInfo> parserModelInfo(JSONObject jsonObject) throws JSONException {
//		NewsDBManager dbManager=NewsDBManager.getNewsDBManager(context);
        ArrayList<ModelInfo> modelInfos = new ArrayList<ModelInfo>();

        Log.i("tag", "medol:" + jsonObject.toString());

//		//获取所有数据
//		JSONObject jsonObject=new JSONObject(result);
        //获取一个数组 根据名称
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        //循环遍历这个数组
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject object = jsonArray.getJSONObject(i);
            int id = object.getInt("id");
            String name = object.getString("name");
            String cover = CommonUtils.IMAGES_LIST + object.getString("cover");
            String code = object.getString("code");
            ArrayList<ModelComponetInfo> components = new ArrayList<ModelComponetInfo>();
            JSONArray jsonArray2 = object.getJSONArray("components");

            for (int j = 0; j < jsonArray2.length(); j++) {

                JSONObject object1 = jsonArray2.getJSONObject(j);

                ModelComponetInfo component = getComponetInfo(object1);
                components.add(component);
            }

            ModelInfo modelInfo = new ModelInfo();
            modelInfo.setCode(code);
            modelInfo.setId(id);
            modelInfo.setName(name);
            modelInfo.setCover(cover);
            modelInfo.setComponent_detail(components);
            modelInfos.add(modelInfo);
        }
        return modelInfos;
    }

    private ModelComponetInfo getComponetInfo(JSONObject object1) throws JSONException {
        ModelComponetInfo info = new ModelComponetInfo();
        info.setId(object1.getInt("id"));
        info.setType(object1.getString("type"));
        info.setZ_index(object1.getInt("z_index"));
        info.setX(object1.getInt("x"));
        info.setY(object1.getInt("y"));
        info.setW(object1.getInt("w"));
        info.setH(object1.getInt("h"));
        info.setAnchor_x(object1.getInt("anchor_x"));
        info.setAnchor_y(object1.getInt("anchor_y"));
        info.setPic_mode(object1.getString("pic_mode"));
        info.setPic_num(object1.getInt("pic_num"));
        String pics = object1.getString("pics");
        info.setPics(splitpic(pics));
        return info;
    }

    public ArrayList<SimpleSenceInfo> parserSimpleSenceInfos(JSONObject jsonObject) throws JSONException {
        ArrayList<SimpleSenceInfo> infos = new ArrayList<SimpleSenceInfo>();
        JSONArray jsonArray = jsonObject.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            SimpleSenceInfo info = new SimpleSenceInfo();
            info.setId(object.getString("id"));
            info.setViewCount(object.getInt("viewTimes"));
            info.setShareCount(object.getInt("shareTimes"));
            info.setUploadTime(object.getString("uploadTime"));
            info.setTitle(object.getString("title"));
            info.setCover(CommonUtils.IMAGES_LIST + object.getString("cover"));
            info.setDescription(object.getString("description"));
            info.setMusic(object.getString("music"));
            info.setPublic(object.getBoolean("is_public"));
            info.setPublicVisable(object.getBoolean("publicVisable"));
            info.setRecommond(object.getBoolean("is_recommond"));
            info.setUpload(true);
            JSONArray jsonArray2 = object.getJSONArray("pages");
            ArrayList<SencePageInfo> pages = new ArrayList<SencePageInfo>();
            for (int j = 0; j < jsonArray2.length(); j++) {
                JSONObject object1 = jsonArray2.getJSONObject(j);
                SencePageInfo pageInfo = new SencePageInfo();
                pageInfo.setIndex(object1.getInt("index"));
                ArrayList<SenceComponetInfo> senceComponetInfos = new ArrayList<SenceComponetInfo>();
                JSONArray jsonArray3 = object1.getJSONArray("components");
                for (int k = 0; k < jsonArray3.length(); k++) {
                    JSONObject object2 = jsonArray3.getJSONObject(k);
                    SenceComponetInfo componetInfo = getSenceComponetInfo(object2);
                    senceComponetInfos.add(componetInfo);
                }
                pageInfo.setComponetInfos(senceComponetInfos);
                pages.add(pageInfo);
            }
            info.setPages(pages);
            infos.add(info);
        }
        return infos;
    }

    private SenceComponetInfo getSenceComponetInfo(JSONObject object1) throws JSONException {
        SenceComponetInfo info = new SenceComponetInfo();
        info.setId(object1.getString("id"));
        info.setType(object1.getString("type"));
        info.setZ_index(object1.getInt("z_index"));
        info.setX(object1.getInt("x"));
        info.setY(object1.getInt("y"));
        info.setW(object1.getInt("w"));
        info.setH(object1.getInt("h"));
        info.setRotate(object1.getInt("rotate"));
        info.setAnchor_x(object1.getInt("anchor_x"));
        info.setAnchor_y(object1.getInt("anchor_y"));
        info.setPic_mode(object1.getString("pic_mode"));
        info.setPic_num(object1.getInt("pic_num"));
        info.setText_content(object1.getString("text_content"));
        info.setText_color(object1.getInt("text_color"));
        String pics = splitpic(splitpic(object1.getString("pics")));
        info.setPics(pics);
        return info;
    }

    private String splitpic(String pic) {
        String[] pics = pic.split(";");
        String p = "";
        for (int i = 0; i < pics.length; i++) {
            p += CommonUtils.IMAGES_LIST + pics[i] + ";";
        }
        p = p.substring(0, p.length() - 1);
        return p;
    }
}
