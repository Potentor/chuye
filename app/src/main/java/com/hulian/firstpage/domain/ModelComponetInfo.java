package com.hulian.firstpage.domain;

import java.io.Serializable;

/**
 * 模版中的子控件信息
 * */
public class ModelComponetInfo implements Serializable{

    int id;
    String type;
    int z_index;
    int x;
    int y;
    int w;
    int h;
    int anchor_x;
    int anchor_y;
    String pic_mode;
    int pic_num;
    String pics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!(o instanceof ModelComponetInfo)) return false;

        ModelComponetInfo that = (ModelComponetInfo) o;

        if (anchor_x != that.anchor_x) return false;
        if (anchor_y != that.anchor_y) return false;
        if (h != that.h) return false;
        if (id != that.id) return false;
        if (pic_num != that.pic_num) return false;
        if (w != that.w) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z_index != that.z_index) return false;
        if (!pic_mode.equals(that.pic_mode)) return false;
        if (!pics.equals(that.pics)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type.hashCode();
        result = 31 * result + z_index;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + w;
        result = 31 * result + h;
        result = 31 * result + anchor_x;
        result = 31 * result + anchor_y;
        result = 31 * result + pic_mode.hashCode();
        result = 31 * result + pic_num;
        result = 31 * result + pics.hashCode();
        return result;
    }
}
