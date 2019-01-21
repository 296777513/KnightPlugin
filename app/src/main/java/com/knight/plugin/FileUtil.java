package com.knight.plugin;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "PlayCamera";

    public static String initPath(String directoryName) {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + directoryName + "/";
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }
}
