package com.hulian.firstpage.util;

import android.content.Context;
import android.content.pm.ComponentInfo;

import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

/**
 * Created by Administrator on 2015/4/27.
 */
public class JsonUtil {
    Context mContext;
    public static final String SCRATCH_CARD_IMAGE = CommonUtils.IMAGES_LIST + "7105ec098557b74f450b2189ef13c22c.jpg";


    private static JSONObject getComponetJson(SenceComponetInfo componet) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", componet.getId())
                    .put("type", componet.getType())
                    .put("z_index", componet.getZ_index())
                    .put("x", componet.getX())
                    .put("y", componet.getY())
                    .put("w", componet.getW())
                    .put("h", componet.getH())
                    .put("text_color", componet.getText_color())
                    .put("text_content", componet.getText_content())
                    .put("rotate", componet.getRotate())
                    .put("anchor_x", componet.getAnchor_x())
                    .put("anchor_y", componet.getAnchor_y())
                    .put("pic_mode", componet.getPic_mode())
                    .put("pic_num", componet.getPic_num())
                    .put("pics", componet.getjsonpic());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static JSONObject getPageJson(SencePageInfo page) {
        JSONObject json = null;
        try {
            json = new JSONObject().put("id", page.getId())
                    .put("index", page.getIndex()).put("template_id", page.getTemplate_id());

            JSONArray array = new JSONArray();
            Collections.sort(page.getComponetInfos());
            for (SenceComponetInfo componet : page.getComponetInfos()) {
                array.put(getComponetJson(componet));
            }
//            DBManager dbManager = DBManager.getDBManager(mContext);
//            ModelInfo modelInfo = dbManager.queryModelByModelId(page.getTemplate_id());
//            if (modelInfo.getCode().equals(CommonUtils.CODE_MODEL_SCRATCH_CARD)) {
//                SenceComponetInfo com = new SenceComponetInfo();
//                com.setZ_index(1);
//                com.setId(UUIDUtil.getUUID());
//                com.setType("0");
//                com.setW(100);
//                com.setH(100);
//                com.setX(0);
//                com.setY(0);
//                com.setAnchor_x(85);
//                com.setAnchor_y(5);
//                com.setPic_mode("0");
//                com.setPics(SCRATCH_CARD_IMAGE);
//                array.put(getComponetJson(com));
//            }
            json.put("components", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject getSenceJson(SimpleSenceInfo sence) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", sence.getId())
                    .put("title", sence.getTitle())
                    .put("cover", sence.getCover().substring(sence.getCover().lastIndexOf("/") + 1, sence.getCover().length()))
                    .put("description", sence.getDescription())
                    .put("music", sence.getMusic())
                    .put("is_public", sence.isPublic())
                    .put("is_recommond", sence.isRecommond());
            JSONArray array = new JSONArray();
            for (SencePageInfo page : sence.getPages()) {
                array.put(getPageJson(page));

            }
            json.put("pages", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
