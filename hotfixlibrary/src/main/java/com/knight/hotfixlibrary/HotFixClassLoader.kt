package com.knight.hotfixlibrary

import android.util.Log

class HotFixClassLoader(parent: ClassLoader) : ClassLoader(parent) {

    override fun findClass(name: String?): Class<*> {
        Log.i("liyachao", "HotFixClassLoader findClass: $name")
        return super.findClass(name)
    }

    override fun loadClass(name: String?): Class<*> {
        Log.i("liyachao", "HotFixClassLoader loadClass: $name")

        return super.loadClass(name)
    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*> {
        Log.i("liyachao", "HotFixClassLoader loadClass: $name")

        return super.loadClass(name, resolve)
    }


    companion object {
        @JvmStatic
        fun inject(classLoader: ClassLoader) {
            val hotfixClassLoader = HotFixClassLoader(classLoader.parent)
            val parentField = ClassLoader::class.java.getDeclaredField("parent")
            parentField.isAccessible = true
            parentField.set(classLoader, hotfixClassLoader)
            Log.i("liyachao", "success")

        }
    }

}