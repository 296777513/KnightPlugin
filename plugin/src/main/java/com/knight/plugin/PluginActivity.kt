package com.knight.plugin

import android.app.Activity
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class PluginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin)
        Log.i("liyachao", "PluginActivity onCreate")

        findViewById<TextView>(R.id.text).setOnClickListener {
            Toast.makeText(this, R.string.me, Toast.LENGTH_SHORT).show();

        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("liyachao", "PluginActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("liyachao", "PluginActivity onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.i("liyachao", "PluginActivity onPause")
    }


    override fun getResources(): Resources {
        return if (application != null && application.resources != null) application.resources else super.getResources()
    }
}
