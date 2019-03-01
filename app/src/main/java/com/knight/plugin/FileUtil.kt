package com.knight.plugin

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.*

object FileUtil {
    private val parentPath = Environment.getExternalStorageDirectory()
    private var storagePath = ""
    private val DST_FOLDER_NAME = "PlayCamera"

    fun initPath(directoryName: String): String {
        if (storagePath == "") {
            storagePath = parentPath.absolutePath + "/" + directoryName + "/"
            val f = File(storagePath)
            if (!f.exists()) {
                f.mkdir()
            }
        }
        return storagePath
    }

    fun copyData2File(filesDir: File, assets: AssetManager, fileName: String) {
        val file = File(filesDir, fileName)
        if (file.exists()) {
            return
        }
        var outputStream: OutputStream? = null
        var inputStream: InputStream? = null

        try {
            outputStream = FileOutputStream(file)
            inputStream = assets.open(fileName)
            val buffer = ByteArray(1024)
            var len = inputStream.read(buffer)
            while (len != -1) {
                outputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
            Log.i("liyachao", "copy $fileName success")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream?.close()
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
