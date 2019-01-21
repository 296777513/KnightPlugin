package com.knight.plugin

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main1.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        findViewById<Button>(R.id.btn).setOnClickListener {
            startActivity(Intent(this, Class.forName("com.knight.plugin.PluginActivity")))
        }
    }
}
