package com.hulian.firstpage.util;

        import android.content.Context;
        import android.util.TypedValue;

public class CommonUtils {
    private static final String tag = "CommonUtils";

    public static final String IMAGES_LIST = "http://chuye.b0.upaiyun.com/";

    public static final String SENCE_PATH="http://cjl2015.sinaapp.com/1/cjl2015/";
    public static final String TEMPLATE_LIST_PATH = SENCE_PATH+"template/list.json";
    public static final String SENCE_VIEW_PATH = SENCE_PATH+"scene/view/";
    public static final String SENCE_LIST_PATH=SENCE_PATH+"scene/list.json";
    //http://192.168.199.199:8000/Scenes/scene/view/a1aa24353d0240eaa42dbb04dad757bf
//http://192.168.199.199:8000/Scenes/scene/list;jsessionid=6E4B536C47C753E422BA1E68163B9B87?accountId=1
    public static final String SENCE_UPLOAD = SENCE_PATH+"scene/upload/";
    public static final String DEL_SENCE_PATH="http://cjl2015.sinaapp.com/1/cjl2015/scene/delete/";
    public static final String CODE_MODEL_SCRATCH_CARD = "scratch-card";


    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());

    }
}

