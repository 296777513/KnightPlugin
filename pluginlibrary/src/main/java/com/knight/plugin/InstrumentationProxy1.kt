package com.knight.plugin

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.util.Log

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class InstrumentationProxy1(
    private val mInstrumentation: Instrumentation,
    private val mPackageManager: PackageManager
) : Instrumentation() {

    open fun execStartActivity(
        who: Context?, contextThread: IBinder?, token: IBinder, target: Activity?,
        intent: Intent, requestCode: Int, options: Bundle?
    ): Instrumentation.ActivityResult? {
        Log.i("liyachao", "start")
        var resolveInfo: List<ResolveInfo>? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            resolveInfo = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        } else {
            resolveInfo = mPackageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }
        //判断启动的插件Activity是否在AndroidManifest.xml中注册过
        if (null == resolveInfo || resolveInfo.isEmpty()) {
            //保存目标插件
            intent.putExtra(REQUEST_TARGET_INTENT_NAME, intent.component!!.className)
            //设置为占坑Activity
            intent.setClassName(who, PlaceHolderActivity::class.java.name)
            Log.i("liyachao", PlaceHolderActivity::class.java.name)
        }

        try {
            val execStartActivity = Instrumentation::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java, IBinder::class.java, IBinder::class.java, Activity::class.java,
                Intent::class.java, Int::class.java, Bundle::class.java
            )
            return execStartActivity.invoke(
                mInstrumentation,
                who,
                contextThread,
                token,
                target,
                intent,
                requestCode,
                options
            ) as Instrumentation.ActivityResult
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        val intentName = intent?.getStringExtra(REQUEST_TARGET_INTENT_NAME)
        return if (!TextUtils.isEmpty(intentName)) {
            mInstrumentation.newActivity(cl, intentName, intent)
        } else mInstrumentation.newActivity(cl, className, intent)
    }

    companion object {

        const val REQUEST_TARGET_INTENT_NAME = "request_target_intent_name"
    }

}

