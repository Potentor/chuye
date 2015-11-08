package com.hulian.firstpage.domain;

import android.graphics.Color;

import com.hulian.firstpage.util.UUIDUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/3/20.
 */
public class SenceComponetInfo implements Serializable, Comparable<SenceComponetInfo> {
    String id;
    String type;
    int z_index;
    int x;
    int y;
    int w;
    int h;
    float rotate;
    int anchor_x;
    int anchor_y;
    String pic_mode;
    int pic_num;
    String pics;
    int text_color;
    String text_content;

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public int getText_color() {
        return text_color;
    }

    public void setText_color(int text_color) {
        this.text_color = text_color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZ_index() {
        return z_index;
    }

    public void setZ_index(int z_index) {
        this.z_index = z_index;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public int getAnchor_x() {
        return anchor_x;
    }

    public void setAnchor_x(int anchor_x) {
        this.anchor_x = anchor_x;
    }

    public int getAnchor_y() {
        return anchor_y;
    }

    public void setAnchor_y(int anchor_y) {
        this.anchor_y = anchor_y;
    }

    public String getPic_mode() {
        return pic_mode;
    }

    public void setPic_mode(String pic_mode) {
        this.pic_mode = pic_mode;
    }

    public int getPic_num() {
        return pic_num;
    }

    public void setPic_num(int pic_num) {
        this.pic_num = pic_num;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SenceComponetInfo)) return false;

        SenceComponetInfo that = (SenceComponetInfo) o;

        if (anchor_x != that.anchor_x) return false;
        if (anchor_y != that.anchor_y) return false;
        if (h != that.h) return false;
        if (id != that.id) return false;
        if (pic_num != that.pic_num) return false;
        if (rotate != that.rotate) return false;
        if (w != that.w) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z_index != that.z_index) return false;
        if (!pic_mode.equals(that.pic_mode)) return false;
        if (!pics.equals(that.pics)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static SenceComponetInfo createSenceComponet(ModelComponetInfo info) {
        SenceComponetInfo senceComponetInfo = new SenceComponetInfo();
        senceComponetInfo.setId(UUIDUtil.getUUID());
        senceComponetInfo.setType(info.getType());
        senceComponetInfo.setText_content(null);
        senceComponetInfo.setText_color(Color.BLACK);
        senceComponetInfo.setRotate(0);
        senceComponetInfo.setAnchor_x(info.getAnchor_x());
        senceComponetInfo.setAnchor_y(info.getAnchor_y());
        senceComponetInfo.setH(info.getH());
        senceComponetInfo.setPic_mode(info.getPic_mode());
        senceComponetInfo.setPic_num(info.getPic_num());
        senceComponetInfo.setPics(info.pics);
        senceComponetInfo.setW(info.getW());
        senceComponetInfo.setX(info.getX());
        senceComponetInfo.setY(info.getY());
        senceComponetInfo.setZ_index(info.getZ_index());
        return senceComponetInfo;
    }

    @Override
    public int compareTo(SenceComponetInfo sence) {
        if (this.z_index > sence.z_index) {
            return 1;
        } else if (this.z_index == sence.z_index) {
            return 0;
        } else if (this.z_index < sence.z_index) {
            return -1;
        }
        return 0;
    }


    public String getjsonpic() {
        String[] pics = this.getPics().split(";");
        String jsonpics = "";
        for (String pic : pics) {
            pic = pic.substring(pic.lastIndexOf("/") + 1, pic.length());
            jsonpics += pic + ";";
        }
        jsonpics = jsonpics.substring(0, jsonpics.length() - 1);
        return jsonpics;
    }

}
