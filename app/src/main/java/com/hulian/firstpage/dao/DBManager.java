package com.hulian.firstpage.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hulian.firstpage.domain.ModelComponetInfo;
import com.hulian.firstpage.domain.ModelInfo;
import com.hulian.firstpage.domain.SenceComponetInfo;
import com.hulian.firstpage.domain.SencePageInfo;
import com.hulian.firstpage.domain.SimpleSenceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类
 *
 * @author Administrator
 */
public class DBManager {

    // 单例模式
    private static DBManager dbManager;
    private DBOpenHelper helper;

    private DBManager(Context context) {
        helper = new DBOpenHelper(context);
    }

    // 同步锁
    public static DBManager getDBManager(Context context) {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager(context);
                }
            }
        }
        return dbManager;
    }

    /**
     * 添加
     */
    public void insertModel(ModelInfo model) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", model.getId());
        values.put("name", model.getName());
        values.put("code", model.getCode());
        values.put("cover", model.getCover());
        db.insert(DBOpenHelper.MODEL_TABLE, null, values);
        ArrayList<ModelComponetInfo> componets = model.getComponent_detail();
        ContentValues values1 = new ContentValues();
        for (int i = 0; i < componets.size(); i++) {
            ModelComponetInfo componet = componets.get(i);
            values1.put("id", componet.getId());
            values1.put("type", componet.getType());
            values1.put("z_index", componet.getZ_index());
            values1.put("x", componet.getX());
            values1.put("y", componet.getY());
            values1.put("w", componet.getW());
            values1.put("h", componet.getH());
            values1.put("anchor_x", componet.getAnchor_x());
            values1.put("anchor_y", componet.getAnchor_y());
            values1.put("pic_mode", componet.getPic_mode());
            values1.put("pic_num", componet.getPic_num());
            values1.put("pics", componet.getPics());
            values1.put("modelId", model.getId());
            db.insert(DBOpenHelper.MODEL_COMPONET_TABLE, null, values1);
        }

        db.close();
    }

    /**
     * 数据数量
     */
    public long getModelCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DBOpenHelper.MODEL_TABLE, null);
        long len = 0;
        if (cursor.moveToFirst()) {
            len = cursor.getLong(0);
        }
        cursor.close();
        db.close();
        return len;
    }

    /**
     * 数据数量
     */
    public long getSenceCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DBOpenHelper.SIMPLESENCE_TABLE, null);
        long len = 0;
        if (cursor.moveToFirst()) {
            len = cursor.getLong(0);
        }
        cursor.close();
        db.close();
        return len;
    }

    public ModelInfo queryModelByModelId(int id) {
        ModelInfo model = new ModelInfo();
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlModel = "select * from " + DBOpenHelper.MODEL_TABLE + " where id = " + id;

        Cursor cursor = db.rawQuery(sqlModel, null);
        if (cursor.moveToFirst()) {

            int modelId = cursor.getInt(cursor.getColumnIndex("id"));
            String modelName = cursor.getString(cursor.getColumnIndex("name"));
            String modelCover = cursor.getString(cursor.getColumnIndex("cover"));
            String modelCode = cursor.getString(cursor.getColumnIndex("code"));
            ArrayList<ModelComponetInfo> componets = new ArrayList<ModelComponetInfo>();

            String sqlComponet = "select * from " + DBOpenHelper.MODEL_COMPONET_TABLE + " where modelId = " + modelId;
            Cursor cursor1 = db.rawQuery(sqlComponet, null);
            if (cursor1.moveToFirst()) {
                do {

                    ModelComponetInfo info = new ModelComponetInfo();
                    info.setId(cursor1.getInt(cursor1.getColumnIndex("id")));
                    info.setType(cursor1.getString(cursor1.getColumnIndex("type")));
                    info.setZ_index(cursor1.getInt(cursor1.getColumnIndex("z_index")));
                    info.setX(cursor1.getInt(cursor1.getColumnIndex("x")));
                    info.setY(cursor1.getInt(cursor1.getColumnIndex("y")));
                    info.setW(cursor1.getInt(cursor1.getColumnIndex("w")));
                    info.setH(cursor1.getInt(cursor1.getColumnIndex("h")));
                    info.setAnchor_x(cursor1.getInt(cursor1.getColumnIndex("anchor_x")));
                    info.setAnchor_y(cursor1.getInt(cursor1.getColumnIndex("anchor_y")));
                    info.setPic_mode(cursor1.getString(cursor1.getColumnIndex("pic_mode")));
                    info.setPic_num(cursor1.getInt(cursor1.getColumnIndex("pic_num")));
                    info.setPics(cursor1.getString(cursor1.getColumnIndex("pics")));
                    componets.add(info);
                } while (cursor1.moveToNext());
                cursor1.close();
            }
            model.setId(modelId);
            model.setName(modelName);
            model.setCover(modelCover);
            model.setCode(modelCode);
            model.setComponent_detail(componets);


            cursor.close();
            db.close();
        }
        return model;


    }

    /**
     * 查询数据
     */
    public ArrayList<ModelInfo> queryModel() {
        ArrayList<ModelInfo> modelList = new ArrayList<ModelInfo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String sqlModel = "select * from " + DBOpenHelper.MODEL_TABLE;

        Cursor cursor = db.rawQuery(sqlModel, null);
        if (cursor.moveToFirst()) {
            do {
                int modelId = cursor.getInt(cursor.getColumnIndex("id"));
                String modelName = cursor.getString(cursor.getColumnIndex("name"));
                String modelCover = cursor.getString(cursor.getColumnIndex("cover"));
                ArrayList<ModelComponetInfo> componets = new ArrayList<ModelComponetInfo>();

                String sqlComponet = "select * from " + DBOpenHelper.MODEL_COMPONET_TABLE + " where modelId = " + modelId;
                Cursor cursor1 = db.rawQuery(sqlComponet, null);
                if (cursor1.moveToFirst()) {
                    do {

                        ModelComponetInfo info = new ModelComponetInfo();
                        info.setId(cursor1.getInt(cursor1.getColumnIndex("id")));
                        info.setType(cursor1.getString(cursor1.getColumnIndex("type")));
                        info.setZ_index(cursor1.getInt(cursor1.getColumnIndex("z_index")));
                        info.setX(cursor1.getInt(cursor1.getColumnIndex("x")));
                        info.setY(cursor1.getInt(cursor1.getColumnIndex("y")));
                        info.setW(cursor1.getInt(cursor1.getColumnIndex("w")));
                        info.setH(cursor1.getInt(cursor1.getColumnIndex("h")));
                        info.setAnchor_x(cursor1.getInt(cursor1.getColumnIndex("anchor_x")));
                        info.setAnchor_y(cursor1.getInt(cursor1.getColumnIndex("anchor_y")));
                        info.setPic_mode(cursor1.getString(cursor1.getColumnIndex("pic_mode")));
                        info.setPic_num(cursor1.getInt(cursor1.getColumnIndex("pic_num")));
                        info.setPics(cursor1.getString(cursor1.getColumnIndex("pics")));
                        componets.add(info);
                    } while (cursor1.moveToNext());
                    cursor1.close();
                }
                ModelInfo model = new ModelInfo();
                model.setId(modelId);
                model.setCover(modelCover);
                model.setName(modelName);
                model.setComponent_detail(componets);

                modelList.add(model);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return modelList;
    }

    public void insertSimpleSence(SimpleSenceInfo simpleSenceInfo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("id", simpleSenceInfo.getId());
        values.put("title", simpleSenceInfo.getTitle());
        values.put("view_times", simpleSenceInfo.getViewCount());
        values.put("share_times", simpleSenceInfo.getShareCount());
        values.put("upload_time", simpleSenceInfo.getUploadTime());
        values.put("cover", simpleSenceInfo.getCover());
        values.put("description", simpleSenceInfo.getDescription());
        values.put("music", simpleSenceInfo.getMusic());
        values.put("isPublic", simpleSenceInfo.isPublic() + "");
        values.put("isUpload", simpleSenceInfo.isUpload() + "");
        values.put("isRecommond", simpleSenceInfo.isRecommond() + "");


        db.insert(DBOpenHelper.SIMPLESENCE_TABLE, null, values);
        ArrayList<SencePageInfo> pages = simpleSenceInfo.getPages();
        ContentValues values1 = new ContentValues();
        if (pages != null) {
            for (int i = 0; i < pages.size(); i++) {
                ArrayList<SenceComponetInfo> componets = pages.get(i).getComponetInfos();
                values1.put("pageIndex", pages.get(i).getIndex());
                values1.put("id", pages.get(i).getId());
                values1.put("senceID", simpleSenceInfo.getId());
                values1.put("template_id", pages.get(i).getTemplate_id());
                db.insert(DBOpenHelper.SENCE_PAGE_TABLE, null, values1);
                ContentValues values2 = new ContentValues();
                for (int j = 0; j < componets.size(); j++) {
                    SenceComponetInfo componet = componets.get(j);

                    values2.put("id", componet.getId());
                    values2.put("type", componet.getType());
                    values2.put("z_index", componet.getZ_index());
                    values2.put("x", componet.getX());
                    values2.put("y", componet.getY());
                    values2.put("w", componet.getW());
                    values2.put("h", componet.getH());
                    values2.put("rotate", componet.getRotate());
                    values2.put("anchor_x", componet.getAnchor_x());
                    values2.put("anchor_y", componet.getAnchor_y());
                    values2.put("pic_mode", componet.getPic_mode());
                    values2.put("pic_num", componet.getPic_num());
                    values2.put("pics", componet.getPics());
                    values2.put("text_content", componet.getText_content());
                    values2.put("text_color", componet.getText_color());
                    values2.put("senceID", simpleSenceInfo.getId());
                    values2.put("pageIndex", pages.get(i).getIndex());
                    db.insert(DBOpenHelper.SENCE_COMPONET_TABLE, null, values2);
                }
            }
        }
        db.close();
        Log.e("db", "scene insert");
    }

    public SimpleSenceInfo querySenceById(String id) {
        SimpleSenceInfo simpleSenceInfo = new SimpleSenceInfo();
        String sqlSence = "select * from " + DBOpenHelper.SIMPLESENCE_TABLE + " where id = '" + id + "'";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlSence, null);
        if (cursor.moveToFirst()) {


            simpleSenceInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
            simpleSenceInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            simpleSenceInfo.setViewCount(cursor.getInt(cursor.getColumnIndex("view_times")));
            simpleSenceInfo.setShareCount(cursor.getInt(cursor.getColumnIndex("share_times")));
            simpleSenceInfo.setUploadTime(cursor.getString(cursor.getColumnIndex("upload_time")));
            simpleSenceInfo.setCover(cursor.getString(cursor.getColumnIndex("cover")));
            simpleSenceInfo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            simpleSenceInfo.setMusic(cursor.getString(cursor.getColumnIndex("music")));
            simpleSenceInfo.setPublic(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isPublic"))));
            simpleSenceInfo.setUpload(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isUpload"))));
            simpleSenceInfo.setRecommond(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isRecommond"))));

            ArrayList<SencePageInfo> sencePages = new ArrayList<>();
            String sqlPage = "select * from " + DBOpenHelper.SENCE_PAGE_TABLE + " where senceID = '" + simpleSenceInfo.getId() + "'";
            Cursor cursor1 = db.rawQuery(sqlPage, null);
            if (cursor1.moveToFirst()) {

                do {
                    int index = cursor1.getInt(cursor1.getColumnIndex("pageIndex"));
                    String pageId = cursor1.getString(cursor1.getColumnIndex("id"));
                    SencePageInfo sencePage = getSencePage(simpleSenceInfo.getId(), index);
                    int template_id = cursor1.getInt(cursor1.getColumnIndex("template_id"));

                    sencePage.setId(pageId);
                    sencePage.setTemplate_id(template_id);
                    sencePages.add(sencePage);
                } while (cursor1.moveToNext());
                cursor1.close();
                simpleSenceInfo.setPages(sencePages);
            }


            cursor.close();
            db.close();
        }
        return simpleSenceInfo;

    }

    /**
     * 查询数据
     */
    public ArrayList<SimpleSenceInfo> querySence() {
        ArrayList<SimpleSenceInfo> sencelist = new ArrayList<SimpleSenceInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sqlSence = "select * from " + DBOpenHelper.SIMPLESENCE_TABLE;

        Cursor cursor = db.rawQuery(sqlSence, null);
        if (cursor.moveToFirst()) {
            do {
                SimpleSenceInfo simpleSenceInfo = new SimpleSenceInfo();
                simpleSenceInfo.setId(cursor.getString(cursor.getColumnIndex("id")));
                simpleSenceInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                simpleSenceInfo.setViewCount(cursor.getInt(cursor.getColumnIndex("view_times")));
                simpleSenceInfo.setShareCount(cursor.getInt(cursor.getColumnIndex("share_times")));
                simpleSenceInfo.setUploadTime(cursor.getString(cursor.getColumnIndex("upload_time")));
                simpleSenceInfo.setCover(cursor.getString(cursor.getColumnIndex("cover")));
                simpleSenceInfo.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                simpleSenceInfo.setMusic(cursor.getString(cursor.getColumnIndex("music")));
                simpleSenceInfo.setPublic(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isPublic"))));
                simpleSenceInfo.setUpload(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isUpload"))));
                simpleSenceInfo.setRecommond(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isRecommond"))));

                ArrayList<SencePageInfo> sencePages = new ArrayList<>();
                String sqlPage = "select * from " + DBOpenHelper.SENCE_PAGE_TABLE + " where senceID = '" + simpleSenceInfo.getId() + "'";
                Cursor cursor1 = db.rawQuery(sqlPage, null);
                if (cursor1.moveToFirst()) {

                    do {
                        int index = cursor1.getInt(cursor1.getColumnIndex("pageIndex"));
                        String id = cursor1.getString(cursor1.getColumnIndex("id"));
                        SencePageInfo sencePage = getSencePage(simpleSenceInfo.getId(), index);
                        sencePage.setId(id);
                        sencePages.add(sencePage);
                    } while (cursor1.moveToNext());
                    cursor1.close();
                    simpleSenceInfo.setPages(sencePages);
                }
                sencelist.add(simpleSenceInfo);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        Log.e("db", "scene query");
//        Log.e("query",sencelist.toString());
        return sencelist;
    }

    private SencePageInfo getSencePage(String simpleSenceInfoID, int pageIndex) {
        SQLiteDatabase db = helper.getReadableDatabase();
        SencePageInfo sencePage = new SencePageInfo();
        sencePage.setIndex(pageIndex);
        ArrayList<SenceComponetInfo> componets = new ArrayList<>();
        String sqlComponet = "select * from " + DBOpenHelper.SENCE_COMPONET_TABLE + " where senceID = '" + simpleSenceInfoID + "'and pageIndex ='" + pageIndex + "'";
        Cursor cursor2 = db.rawQuery(sqlComponet, null);
        if (cursor2.moveToFirst()) {
            do {
                SenceComponetInfo info = new SenceComponetInfo();
                info.setId(cursor2.getString(cursor2.getColumnIndex("id")));
                info.setType(cursor2.getString(cursor2.getColumnIndex("type")));
                info.setZ_index(cursor2.getInt(cursor2.getColumnIndex("z_index")));
                info.setX(cursor2.getInt(cursor2.getColumnIndex("x")));
                info.setY(cursor2.getInt(cursor2.getColumnIndex("y")));
                info.setW(cursor2.getInt(cursor2.getColumnIndex("w")));
                info.setH(cursor2.getInt(cursor2.getColumnIndex("h")));
                info.setRotate(cursor2.getFloat(cursor2.getColumnIndex("rotate")));
                info.setAnchor_x(cursor2.getInt(cursor2.getColumnIndex("anchor_x")));
                info.setAnchor_y(cursor2.getInt(cursor2.getColumnIndex("anchor_y")));
                info.setPic_mode(cursor2.getString(cursor2.getColumnIndex("pic_mode")));
                info.setPic_num(cursor2.getInt(cursor2.getColumnIndex("pic_num")));
                info.setPics(cursor2.getString(cursor2.getColumnIndex("pics")));
                info.setText_content(cursor2.getString(cursor2.getColumnIndex("text_content")));
                info.setText_color(cursor2.getInt(cursor2.getColumnIndex("text_color")));
                componets.add(info);
            } while (cursor2.moveToNext());
            cursor2.close();
            sencePage.setComponetInfos(componets);
        }
        return sencePage;
    }

    public void deleteSimpleSence(SimpleSenceInfo sence) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String id = sence.getId();
        String SQLDeleteSence = "delete from " + DBOpenHelper.SIMPLESENCE_TABLE + " where id = '" + id + "'";
        String SQLDeletePage = "delete from " + DBOpenHelper.SENCE_PAGE_TABLE + " where senceID = '" + id + "'";
        String SQLDeleteComponet = "delete from " + DBOpenHelper.SENCE_COMPONET_TABLE + " where senceID = '" + id + "'";
        db.execSQL(SQLDeleteComponet);
        db.execSQL(SQLDeletePage);
        db.execSQL(SQLDeleteSence);
    }

    public void updateSimpleSence(SimpleSenceInfo sence) {
        deleteSimpleSence(sence);
        insertSimpleSence(sence);
        Log.e("db", "scene updateSimpleSence");
    }

    public void updateSimpleSenceSimpleInfo(SimpleSenceInfo sence) {
        String sql = "UPDATE " + DBOpenHelper.SIMPLESENCE_TABLE + " SET title =  ?,description = ? WHERE id = ?";

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql, new String[]{sence.getTitle(), sence.getDescription(), sence.getId()});
//        List<SimpleSenceInfo> senceInfos = querySence();
//        Log.e("sence",senceInfos.toString());
        Log.e("db", "scene updateSimpleSenceSimpleInfo");
    }

    public SenceComponetInfo queryComponetById(String id) {
        SenceComponetInfo componet = new SenceComponetInfo();
        String sql = "Select * from " + DBOpenHelper.SENCE_COMPONET_TABLE + " where id = '" + id + "'";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToNext()){
            componet.setId(cursor.getString(cursor.getColumnIndex("id")));
            componet.setType(cursor.getString(cursor.getColumnIndex("type")));
            componet.setZ_index(cursor.getInt(cursor.getColumnIndex("z_index")));
            componet.setX(cursor.getInt(cursor.getColumnIndex("x")));
            componet.setY(cursor.getInt(cursor.getColumnIndex("y")));
            componet.setW(cursor.getInt(cursor.getColumnIndex("w")));
            componet.setH(cursor.getInt(cursor.getColumnIndex("h")));
            componet.setRotate(cursor.getFloat(cursor.getColumnIndex("rotate")));
            componet.setAnchor_x(cursor.getInt(cursor.getColumnIndex("anchor_x")));
            componet.setAnchor_y(cursor.getInt(cursor.getColumnIndex("anchor_y")));
            componet.setPic_mode(cursor.getString(cursor.getColumnIndex("pic_mode")));
            componet.setText_content(cursor.getString(cursor.getColumnIndex("text_content")));
            componet.setText_color(cursor.getInt(cursor.getColumnIndex("text_color")));
            componet.setPic_num(cursor.getInt(cursor.getColumnIndex("pic_num")));
            componet.setPics(cursor.getString(cursor.getColumnIndex("pics")));
        }
        cursor.close();
        return  componet;
    }
}
