package com.hulian.firstpage.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 模版信息
 */
public class ModelInfo implements Serializable {
    int id;
    String name;
    String cover;
    String code;
    ArrayList<ModelComponetInfo> component_detail;

    public ModelInfo() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public ArrayList<ModelComponetInfo> getComponent_detail() {
        return component_detail;
    }

    public void setComponent_detail(ArrayList<ModelComponetInfo> component_detail) {
        this.component_detail = component_detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelInfo)) return false;

        ModelInfo modelInfo = (ModelInfo) o;


        if (id != modelInfo.id) return false;
        if (!component_detail.equals(modelInfo.component_detail)) return false;
        if (!cover.equals(modelInfo.cover)) return false;
        if (!name.equals(modelInfo.name)) return false;

        return true;
    }


}
