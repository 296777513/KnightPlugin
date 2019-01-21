package com.knight.plugin

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import com.knight.plugin.utils.HookHelper
import com.knight.plugin.utils.InjectUtil

class MyApplication : Application() {
    var pluginResource: Resources? = null
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            InjectUtil.inject(this, classLoader) // 加载插件Apk的类文件
            HookHelper.hookAMS()
            HookHelper.hookH()
            initPluginResource()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    fun initPluginResource() {
        val clazz = AssetManager::class.java
        val assetManager = clazz.newInstance()
        val method = clazz.getMethod("addAssetPath", String::class.java)
        val absoluteFile = filesDir.listFiles()[0].listFiles()[0].absolutePath
        Log.i("liyachao1", "name: ${absoluteFile}")

        method.invoke(assetManager, absoluteFile)
        pluginResource = Resources(assetManager, resources.displayMetrics, resources.configuration)
    }

    override fun getResources(): Resources {
        return if (pluginResource == null) super.getResources() else pluginResource!!
    }
}