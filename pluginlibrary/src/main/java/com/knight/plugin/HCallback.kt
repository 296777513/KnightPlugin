package com.knight.plugin

import android.content.Intent
import android.os.Handler
import android.os.Message
import com.knight.plugin.utils.FieldUtil

class HCallback : Handler.Callback {
    val LAUNCH_ACTIVITY = 100

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            LAUNCH_ACTIVITY -> {
                try {
                    val obj = msg.obj
                    val intent = FieldUtil.getField(obj.javaClass, obj, "intent") as Intent
                    val targetIntent = intent.getParcelableExtra<Intent>(PluginManager.TRANSFER_INTENT)
                    intent.component = targetIntent.component
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return false
    }

}