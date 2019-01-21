package com.knight.plugin

import android.content.Intent
import android.os.Handler
import android.os.Message
import com.knight.plugin.utils.FieldUtil
import com.knight.plugin.utils.HookHelper

class HCallback : Handler.Callback {
    val LAUNCH_ACTIVITY = 100

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            LAUNCH_ACTIVITY -> {
                try {
                    val obj = msg.obj
                    val intent = FieldUtil.getField(obj.javaClass, obj, "intent") as Intent
                    val targetIntent = intent.getParcelableExtra<Intent>(HookHelper.TRANSFER_INTERNT)
                    intent.component = targetIntent.component
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return false
    }

}