package com.knight.plugin

import android.content.Intent
import android.util.Log
import com.knight.plugin.utils.HookHelper
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

// 动态代理
//class IActivityManagerProxy(val am: Any) : InvocationHandler {
//
//
//    override fun invoke(proxy: Any?, method: Method, args: Array<Any>): Any {
//        Log.e("liyachao", "name: ${method.name}  args1: ${args.size}")
//        if ("startActivity" == method.name) { // startActivity 方法
//            Log.e("liyachao", "args2: ${args.size}")
//            for (i in 0 until (args.size)) {
//                if (args[i] is Intent) {
//                    val newIntent = Intent()
//                    newIntent.setClassName("com.knight.plugin", "com.knight.plugin.SubActivity")
//                    newIntent.putExtra(HookHelper.TRANSFER_INTENT, args[i] as Intent)//保留原始Intent
//                    args[i] = newIntent
//                    return method.invoke(am, args)
//                }
//            }
//        }
//        return method.invoke(am, args)
//    }
//
//}