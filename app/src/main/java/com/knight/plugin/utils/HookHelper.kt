package com.knight.plugin.utils

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import com.knight.plugin.HCallback
import com.knight.plugin.IActivityManagerProxy
import java.lang.Exception
import java.lang.reflect.Proxy

object HookHelper {
    const val TRANSFER_INTENT = "transfer_intent"

    @SuppressLint("PrivateApi")
    @JvmStatic
    @Throws(Exception::class)
    fun hookAMS() {
        val singleton = if (Build.VERSION.SDK_INT >= 26) { //大于等于8.0
            val clazz = Class.forName("android.app.ActivityManager")
            FieldUtil.getField(clazz, null, "IActivityManagerSingleton") //拿到静态字段
        } else { // 8.0以下
            val activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative")
            FieldUtil.getField(activityManagerNativeClazz, null, "gDefault")// get the static field
        }

        val singleClazz = Class.forName("android.util.Singleton")
        val getMethod = singleClazz.getMethod("get")
        val iActivityManager = getMethod.invoke(singleton)
        val iActivityManagerClazz = Class.forName("android.app.IActivityManager")
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(iActivityManagerClazz),
            IActivityManagerProxy(iActivityManager)
        )
        FieldUtil.setField(singleClazz, singleton, "mInstance", proxy)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun hookH() {
        val activityThreadClazz = Class.forName("android.app.ActivityThread")
        val activityThread = FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread")
        val mH = FieldUtil.getField(activityThreadClazz, activityThread, "mH")
        FieldUtil.setField(Handler::class.java, mH, "mCallback", HCallback())
    }
}