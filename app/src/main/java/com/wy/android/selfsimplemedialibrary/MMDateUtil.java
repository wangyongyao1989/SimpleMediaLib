package com.wy.android.selfsimplemedialibrary;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MMDateUtil {

    public static final String FormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    //定义GB的计算常量
    private static final int GB = 1024 * 1024 * 1024;
    //定义MB的计算常量
    private static final int MB = 1024 * 1024;
    //定义KB的计算常量
    private static final int KB = 1024;


    public static String getStringByFormat(long paramLong, String paramString) {
        try {
            paramString = new SimpleDateFormat(paramString).format(Long.valueOf(paramLong));
            return paramString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(FormatYMDHMS);
        return format.format(date);
    }

    private static final String mformatType = "yyyy/MM/dd HH:mm:ss";

    public static String getFileLastModifiedTime(String fileString) {
        File file = new File(fileString);
        Calendar cal = Calendar.getInstance();
        long time = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(mformatType);
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }


    public static String bytes2kb(long bytes) {
        DecimalFormat format = new DecimalFormat("###.0");
        if (bytes / GB >= 1) {
            return format.format(bytes / GB) + "GB";
        } else if (bytes / MB >= 1) {
            return format.format(bytes / MB) + "MB";
        } else if (bytes / KB >= 1) {
            return format.format(bytes / KB) + "KB";
        } else {
            return bytes + "B";
        }
    }

    public static String GetTimeFromLong(long timeLong) {
        long ss = timeLong / 1000;
        long mm = ss / 60;
        long hh = mm / 60;
        String s;
        String m;
        String h;
        if (ss % 60 < 10) {
            s = "0" + ss % 60;
        } else {
            s = ss % 60 + "";
        }

        if (mm % 60 < 10) {
            m = "0" + mm % 60;
        } else {
            m = mm % 60 + "";
        }

        if (hh % 60 < 10) {
            h = "0" + hh % 60;
        } else {
            h = hh % 60 + "";
        }
        return h + ":" + m + ":" + s;
    }


}
