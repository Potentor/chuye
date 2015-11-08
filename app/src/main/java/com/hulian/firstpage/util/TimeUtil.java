package com.hulian.firstpage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/3/30.
 */
public class TimeUtil {
    public static String getNowTime() {
        String format1 = "yyyyMMdd";
        String format2 = "HHmmss";
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
        return sdf1.format(date)+"T"+sdf2.format(date);
    }
}
