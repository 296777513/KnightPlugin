package com.knight.plugin

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.liyachao.permission.KnightPermission
import com.knight.hotfixlibrary.HotFixClassLoader
import com.knight.hotfixlibrary.HotFixManager
import com.knight.plugin.hotfix.Function
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MyApplication : Application() {
    var pluginResource: Resources? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
//            val path = FileUtil.initPath("com.knight.plugin")
            FileUtil.copyData2File(filesDir, assets, "patch.dex")
            FileUtil.copyData2File(filesDir, assets, "plugin.apk")
            val file = File(filesDir.absolutePath)
            var pluginPath = ""
            file.listFiles().forEach {
                if (it.name.endsWith(".apk")) {
                    pluginPath = it.absolutePath
                }
            }
            pluginResource =
                PluginManager.initPlugin(this, pluginPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        KnightPermission.init(this)
    }

    override fun getResources(): Resources {
        return if (pluginResource == null) super.getResources() else pluginResource!!
    }
}