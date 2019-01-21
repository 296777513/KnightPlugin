package com.knight.plugin

import android.content.Intent
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

// 动态代理
class IActivityManagerProxy(val am: Any) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method, args: Array<Any>): Any? {
        if ("startActivity" == method.name) { // startActivity 方法
            for (i in 0 until (args.size)) {
                if (args[i] is Intent) {
                    val newIntent = Intent()
                    val path = PluginManager.placeHolderActivityPath
                    val packageName = path.substring(0, path.lastIndexOf('.'))
                    Log.i("liyachao", "path: $path \n packageName:$packageName")
                    newIntent.setClassName(packageName, path)
                    newIntent.putExtra(PluginManager.TRANSFER_INTENT, args[i] as Intent)//保留原始Intent
                    args[i] = newIntent
                    return method.invoke(am, *args)
                }
            }
        }
        return method.invoke(am, *args)
    }

}