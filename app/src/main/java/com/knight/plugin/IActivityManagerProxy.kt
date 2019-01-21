package com.knight.plugin

import android.content.Intent
import com.knight.plugin.utils.HookHelper
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

// 动态代理
class IActivityManagerProxy(val am: Any) : InvocationHandler {


    override fun invoke(proxy: Any?, method: Method, args: Array<Any>): Any {
        if ("startActivity" == method.name) { // startActivity 方法
            for (i in 0 until (args.size)) {
                args[i].takeIf {
                    it is Intent
                }?.let {
                    val newIntent = Intent()
                    newIntent.setClassName("rocketyly.demo", "rocketly.demo.SubActivity")
                    newIntent.putExtra(HookHelper.TRANSFER_INTERNT, it as Intent)//保留原始Intent
                    args[i] = newIntent
                }
            }
        }

        return method.invoke(am, args)
    }

}