package com.knight.plugin

import android.app.Application
import android.content.Context
import com.knight.plugin.utils.InjectUtil

class MyApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            InjectUtil.inject(this, classLoader) // 加载插件Apk的类文件
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}