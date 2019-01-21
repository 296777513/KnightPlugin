package com.knight.plugin

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.liyachao.permission.KnightPermission
import java.io.File

class MyApplication : Application() {
    var pluginResource: Resources? = null
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            val path = FileUtil.initPath("com.knight.plugin")
            val file = File(path)
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