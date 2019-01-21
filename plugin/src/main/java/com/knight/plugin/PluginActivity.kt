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
        var pluginFile =
            this.filesDir
        Log.i("liyachao1", "path: ${filesDir.listFiles()[0].listFiles()[0].name}")

        pluginFile.listFiles().forEach {
            if (it.name == "plugin"){
                it.listFiles().forEach {
                    Log.i("liyachao", "path: ${it.name}")
                }
            }
        }

//        Log.i("liyachao", "path: ${pluginFile.absoluteFile}")
    }


    override fun getResources(): Resources {
        return if (application != null && application.resources != null) application.resources else super.getResources()
    }
}
