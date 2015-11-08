package com.hulian.firstpage.domain;

import com.hulian.firstpage.util.TimeUtil;
import com.hulian.firstpage.util.UUIDUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户的一个初页
 */
public class SimpleSenceInfo implements Serializable {
    private String id;
    private int viewCount;
    private int shareCount;
    private String uploadTime;
    private String title;
    private String cover;
    private String description;
    private String music;
    private boolean publicVisable;
    private boolean isPublic;
    private boolean isRecommond;
    private boolean isUpload;
    private ArrayList<SencePageInfo> pages;

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getCover() {
        return cover;
    }


    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isRecommond() {
        return isRecommond;
    }

    public void setRecommond(boolean isRecommond) {
        this.isRecommond = isRecommond;
    }

    public boolean isPublicVisable() {
        return publicVisable;
    }

    public void setPublicVisable(boolean publicVisable) {
        this.publicVisable = publicVisable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<SencePageInfo> getPages() {
        return pages;
    }

    public void setPages(ArrayList<SencePageInfo> pages) {
        this.pages = pages;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleSenceInfo)) return false;

        SimpleSenceInfo info = (SimpleSenceInfo) o;

        if (!id.equals(info.id)) return false;
        return true;
    }

    public static SimpleSenceInfo createSence(ModelInfo model) {
        SimpleSenceInfo sence = new SimpleSenceInfo();
        sence.setId(UUIDUtil.getUUID());
        sence.setTitle("我的场景录");
        sence.setCover(model.getCover());
        sence.setUploadTime(TimeUtil.getNowTime());
        ArrayList<SencePageInfo> sencePages = new ArrayList<SencePageInfo>();
        sencePages.add(SencePageInfo.createPage(model));
        sence.setPages(sencePages);
        return sence;
    }
}
