package com.knight.plugin

import android.app.Activity
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log

class PluginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin)
    }


    override fun getResources(): Resources {
        return if (application != null && application.resources != null) application.resources else super.getResources()
    }
}
