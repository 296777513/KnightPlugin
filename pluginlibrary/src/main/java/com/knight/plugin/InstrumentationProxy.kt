package com.knight.plugin

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

//class InstrumentationProxy(
//    val instrumentation: Instrumentation,
//    val packageManager: PackageManager
//) : Instrumentation() {
//
//    fun execStartActivity(
//        who: Context, contextThread: IBinder, token: IBinder, target: Activity?, intent: Intent,
//        requestCode: Int, options: Bundle
//    ): ActivityResult? {
//        val resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
//        Log.i("liyachao111", resolveInfo.toString())
//        if (resolveInfo == null || resolveInfo.isEmpty()) {
//            intent.putExtra("temp", intent.component)
//            Log.i("liyachao111", intent.component.toString())
//            intent.setClassName(who, PlaceHolderActivity::class.java.name)
//        }
//
//        try {
//            val execStartActivity = Instrumentation::class.java.getDeclaredMethod(
//                "execStartActivity",
//                Context::class.java, IBinder::class.java, IBinder::class.java, Activity::class.java,
//                Intent::class.java, Int::class.java, Bundle::class.java
//            )
//
//            return execStartActivity.invoke(
//                instrumentation,
//                who,
//                contextThread,
//                token,
//                target,
//                intent,
//                requestCode,
//                options
//            ) as ActivityResult
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
//        val intentName = intent?.getStringExtra("temp")
//        if (intentName?.isNotEmpty() == true) {
//            return super.newActivity(cl, intentName, intent)
//        }
//        return super.newActivity(cl, className, intent)
//    }
//
//}