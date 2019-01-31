package com.knight.plugin.hotfix;

import android.widget.Toast;
import com.example.liyachao.permission.KnightPermission;
import com.knight.plugin.MyApplication;

public class Function {

    public void test() {
        throw new RuntimeException("error");
//        Toast.makeText(KnightPermission.getCurActivity(),"补丁加载成功",Toast.LENGTH_LONG).show();
    }
}
