package com.knight.plugin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.liyachao.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_main1.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.checkPermissions1({
            setContentView(R.layout.activity_main1)
            findViewById<Button>(R.id.btn).setOnClickListener {
                startActivity(Intent(this, Class.forName("com.knight.plugin.PluginActivity")))
            }
        }, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
