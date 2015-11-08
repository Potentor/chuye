package com.hulian.firstpage.domain;

import com.hulian.firstpage.dao.DBManager;
import com.hulian.firstpage.util.UUIDUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2015/3/30.
 */
public class SencePageInfo implements Serializable {
    private int index;
    private String id;
    int template_id;
    private ArrayList<SenceComponetInfo> componetInfos;

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<SenceComponetInfo> getComponetInfos() {
        return componetInfos;
    }

    public void setComponetInfos(ArrayList<SenceComponetInfo> componetInfos) {
        this.componetInfos = componetInfos;
    }

    public static SencePageInfo createPage(ModelInfo info) {
        SencePageInfo sencePage = new SencePageInfo();
        ArrayList<SenceComponetInfo> componetInfos = new ArrayList<>();
        ArrayList<ModelComponetInfo> modelComponetInfos = info.getComponent_detail();
        for (ModelComponetInfo componet : modelComponetInfos) {
            componetInfos.add(SenceComponetInfo.createSenceComponet(componet));
        }
        sencePage.setComponetInfos(componetInfos);
        sencePage.setId(UUIDUtil.getUUID());
        sencePage.setTemplate_id(info.getId());
        return sencePage;
    }


}
