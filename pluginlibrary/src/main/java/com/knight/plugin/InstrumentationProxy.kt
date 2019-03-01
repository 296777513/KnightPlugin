package com.knight.plugin

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class InstrumentationProxy2(
    val instrumentation: Instrumentation,
    val packageManager: PackageManager
) : Instrumentation() {

    fun execStartActivity(
        who: Context, contextThread: IBinder, token: IBinder, target: Activity?, intent: Intent,
        requestCode: Int, options: Bundle
    ): ActivityResult? {
        Log.i("liyachao111", "start")
        var resolveInfo: List<ResolveInfo>? = null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        } else {
            resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }
        Log.i("liyachao111", resolveInfo.toString())
        if (resolveInfo == null || resolveInfo.isEmpty()) {
            intent.putExtra("temp", intent.component)
            Log.i("liyachao111", intent.component.toString())
            intent.setClassName(who, PlaceHolderActivity::class.java.name)
        }

        try {
            val execStartActivity = Instrumentation::class.java.getDeclaredMethod(
                "execStartActivity",
                Context::class.java, IBinder::class.java, IBinder::class.java, Activity::class.java,
                Intent::class.java, Int::class.java, Bundle::class.java
            )

            return execStartActivity.invoke(
                instrumentation,
                who,
                contextThread,
                token,
                target,
                intent,
                requestCode,
                options
            ) as ActivityResult
        } catch (e: Exception) {
            Log.e("liyachao111", "error : ${e.toString()}")
            e.printStackTrace()
        }
        return null
    }

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        val intentName = intent?.getStringExtra("temp")
        if (intentName?.isNotEmpty() == true) {
            return super.newActivity(cl, intentName, intent)
        }
        return super.newActivity(cl, className, intent)
    }

}