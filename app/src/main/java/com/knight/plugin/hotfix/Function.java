package com.knight.plugin.hotfix;


public class Function {

    public Function() {
        android.util.Log.i("liyachao", "function constructor");

    }

    static {
        android.util.Log.i("liyachao", "function static");
    }

    public void test() {
        throw new RuntimeException("error");
//        Toast.makeText(KnightPermission.getCurActivity(),"补丁加载成功",Toast.LENGTH_LONG).show();
    }
}
