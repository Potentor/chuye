package com.hulian.firstpage.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/4/15.
 */
public class BitMapUtils {
    /**
     * 将剪辑的图片存入sd卡  page/imas
     *
     * @param mFilePath
     * @param mFileName
     * @param paramBitmap
     * @param mFormat
     * @return
     */
    public static boolean saveBitmap(String mFilePath, String mFileName, final Bitmap paramBitmap, final Bitmap.CompressFormat mFormat) {
        FileOutputStream mFileOutPutStream = null;
        try {
            mFileOutPutStream = new FileOutputStream(new File(mFilePath, mFileName));
            paramBitmap.compress(mFormat, 80, mFileOutPutStream);
            mFileOutPutStream.flush();
            mFileOutPutStream.close();

        } catch (IOException mIoexception) {
            try {
                if (mFileOutPutStream != null)
                    mFileOutPutStream.close();
            } catch (IOException exception2) {
            }

            return false;
        }
        return true;

    }
}
