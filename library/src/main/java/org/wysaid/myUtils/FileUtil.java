package org.wysaid.myUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.wysaid.common.Common;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by wangyang on 15/11/27.
 */
public class FileUtil {

    public static final String LOG_TAG = Common.LOG_TAG;

    public static final File externalStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    public static final File videoCacheDirectory = Environment.getDownloadCacheDirectory();
    public static String packageFilesDirectory = null;
    public static String storagePath = null;
    public static String cachePath = null;
    private static String mDefaultFolder = "campusx";

    public static void setDefaultFolder(String defaultFolder) {
        mDefaultFolder = defaultFolder;
    }


    public static String getCachePath(Context context) {
        if (cachePath == null) {
            cachePath = videoCacheDirectory.getAbsolutePath() + "/" + mDefaultFolder;
            File file = new File(cachePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    cachePath = getPathInPackage(context, true);
                }
            }
        }
        return cachePath + "/rec_" + System.currentTimeMillis() + ".mp4";
    }


    public static String getPath(Context context) {

        if (storagePath == null) {
            storagePath = externalStorageDirectory.getAbsolutePath() + "/" + mDefaultFolder;
            File file = new File(storagePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    storagePath = getPathInPackage(context, true);
                }
            }
        }
        return storagePath;
    }

    public static String getPathInPackage(Context context, boolean grantPermissions) {

        if (context == null || packageFilesDirectory != null)
            return packageFilesDirectory;

        //手机不存在sdcard, 需要使用 data/data/name.of.package/files 目录
        String path = context.getFilesDir() + "/" + mDefaultFolder;
        File file = new File(path);

        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(LOG_TAG, "在pakage目录创建" + mDefaultFolder + "临时目录失败!");
                return null;
            }

            if (grantPermissions) {

                //设置隐藏目录权限.
                if (file.setExecutable(true, false)) {
                    Log.i(LOG_TAG, "Package folder is executable");
                }

                if (file.setReadable(true, false)) {
                    Log.i(LOG_TAG, "Package folder is readable");
                }

                if (file.setWritable(true, false)) {
                    Log.i(LOG_TAG, "Package folder is writable");
                }
            }
        }

        packageFilesDirectory = path;
        return packageFilesDirectory;
    }

    public static void saveTextContent(String text, String filename) {
        Log.i(LOG_TAG, "Saving text : " + filename);

        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            fileout.write(text.getBytes());
            fileout.flush();
            fileout.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }
    }
}
